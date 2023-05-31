package co.lemnisk.consumer.service.s3;


import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.model.CDPDestinationInstance;
import co.lemnisk.common.service.DestinationInstanceMappingService;
import co.lemnisk.consumer.service.CDPDestinationApiDetailsService;
import co.lemnisk.consumer.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@EnableRetry
@EnableScheduling
@Service
public class ProcessedFileScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessedFileScheduler.class);

    public HashMap<String, Boolean> alreadyUploadedFilesLemnisk;
    public HashMap<String, Boolean> alreadyUploadedFilesClient;

    public HashMap<String, Integer> failedFilesRetryCountLemnisk;
    public HashMap<String, Integer> failedFilesRetryCountClient;

    @Autowired
    private CDPDestinationApiDetailsService cdpDestinationApiDetailsService;

    @Autowired
    DestinationInstanceMappingService destinationInstanceService;

    @Value("${s3Upload.lemnisk.enable}")
    public boolean isS3UploadEnabledLemnisk;

    @Value("${s3Upload.client.enable}")
    public boolean isS3UploadEnabledClient;

    @Value("${s3Upload.lemnisk.bucket-name}")
    public String lemniskBucketName;

    @Autowired
    EventMonitoring eventMonitoring;

    @PostConstruct
    public void init() {
        alreadyUploadedFilesLemnisk = new HashMap<>();
        alreadyUploadedFilesClient = new HashMap<>();
        failedFilesRetryCountLemnisk = new HashMap<>();
        failedFilesRetryCountClient = new HashMap<>();
    }

    public void refreshFileUploadedList() {

        alreadyUploadedFilesLemnisk.clear();
        alreadyUploadedFilesClient.clear();

        String lemniskUploadedDirectory = FileProcessingHelper.getDirectoryNameByProcessingStage(S3FileProcessingConstants.UPLOADED_LEMNISK);
        String clientUploadedDirectory = FileProcessingHelper.getDirectoryNameByProcessingStage(S3FileProcessingConstants.UPLOADED_CLIENT);

        List<File> lemniskS3UploadedFiles = new ArrayList<>();
        FileUtils.listf(lemniskUploadedDirectory, lemniskS3UploadedFiles);

        List<File> clientS3UploadedFiles = new ArrayList<>();
        FileUtils.listf(clientUploadedDirectory, clientS3UploadedFiles);

        for (File file : lemniskS3UploadedFiles) {
            alreadyUploadedFilesLemnisk.put(file.getName(), true);
        }

        for (File file : clientS3UploadedFiles) {
            alreadyUploadedFilesClient.put(file.getName(), true);
        }
    }


    @Scheduled(cron = "0 0/15 * * * *")
    @Retryable(value = Throwable.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public void processCompletedFiles() throws IOException {

        List<File> completedFiles = new ArrayList<>();
        String PROCESSED_DIRECTORY = FileProcessingHelper.getDirectoryNameByProcessingStage(S3FileProcessingConstants.FILE_PROCESSED);

        FileUtils.listf(PROCESSED_DIRECTORY, completedFiles);

        if (DistributedFileLock.acquire(PROCESSED_DIRECTORY)) {

            uploadFilesToS3(completedFiles);

            LOGGER.info("RELEASING LOCK {}",PROCESSED_DIRECTORY);
            DistributedFileLock.releaseLock(PROCESSED_DIRECTORY);

        } else {
            LOGGER.info("Cannot acquire lock on directory {}",PROCESSED_DIRECTORY);
        }
    }


    private void uploadFilesToS3(List<File> completedFiles) {

        for (File file : completedFiles) {

            refreshFileUploadedList();

            try {

                boolean isFiledUploadedToLemniskBucket = false;
                boolean isFileUploadedToClientBucket = false;

                if (isS3UploadEnabledClient && FileProcessingHelper.isValidCompletedLogFile(file) ) {
                    String config = fetchBucketConfigByDestination(file.getPath());

                    String bucketName = JsonUtils.getPayload(config,"bucket_name").replaceAll("\"","");
                    String arn = JsonUtils.getPayload(config,"role_arn").replaceAll("\"","");
                    String region = JsonUtils.getPayload(config,"region").replaceAll("\"","");

                    if (bucketName.isEmpty() || arn.isEmpty() || region.isEmpty()){
                        throw new TransformerException("Configuration missing either arn/region/bucket_name" + config);
                    }

                    isFileUploadedToClientBucket = uploadToClientS3(file,bucketName, arn, region);
                    LOGGER.info("Client Upload Status {} : File {} : Client Upload Enabled {}", isFileUploadedToClientBucket ,file.getName(),isS3UploadEnabledClient );
                }

                if (isS3UploadEnabledLemnisk && FileProcessingHelper.isValidCompletedLogFile(file) ){
                    isFiledUploadedToLemniskBucket = uploadToS3Lemnisk(file,lemniskBucketName);
                    LOGGER.info("Lemnisk Upload Status {} : File {} : Client Upload Enabled {}", isFiledUploadedToLemniskBucket ,file.getName(), isS3UploadEnabledLemnisk);
                }

                if (isFiledUploadedToLemniskBucket && isFileUploadedToClientBucket && file.exists())
                    file.delete();
                else
                    deleteFileAfterRetry(file);

            } catch (Exception e) {
                LOGGER.error("Exception", e);
            }

        }
    }

    private void processFailedUploads(File file, String failedFor) {

        if (failedFor.equals(S3FileProcessingConstants.UPLOADED_CLIENT)) {
            int retryCount = failedFilesRetryCountClient.get(file.getName()) != null ? failedFilesRetryCountClient.get(file.getName()) : 0;
            if (retryCount >= S3FileProcessingConstants.MAX_RETRIES_S3_UPLOAD) {
                String newDirectory = file.getParent().replace(S3FileProcessingConstants.FILE_PROCESSED, S3FileProcessingConstants.FAILED_UPLOAD_CLIENT);
                FileUtils.copyFile(file, newDirectory + "/" + file.getName());
            }
            retryCount = retryCount + 1;
            LOGGER.info("File: {}, Failed Upload for {}, Retry Count : {}", file, failedFor, retryCount);
            failedFilesRetryCountClient.put(file.getName(), retryCount);
        } else if (failedFor.equals(S3FileProcessingConstants.UPLOADED_LEMNISK)) {

            int retryCount = failedFilesRetryCountLemnisk.get(file.getName()) != null ? failedFilesRetryCountLemnisk.get(file.getName()) : 0;
            if (retryCount >= S3FileProcessingConstants.MAX_RETRIES_S3_UPLOAD) {
                String newDirectory = file.getParent().replace(S3FileProcessingConstants.FILE_PROCESSED, S3FileProcessingConstants.FAILED_UPLOAD_LEMNISK);
                FileUtils.copyFile(file, newDirectory + "/" + file.getName());
            }
            retryCount = retryCount + 1;
            LOGGER.info("File: {}, Failed Upload for {}, Retry Count : {}", file, failedFor, retryCount);
            failedFilesRetryCountLemnisk.put(file.getName(), retryCount);
        }

        LOGGER.warn("File: {}, Failed Upload for {}", file, failedFor);

    }

    private void deleteFileAfterRetry(File file) {

        boolean canDelete = false;

        String fileName = file.getName();
        int failedRetryCountLemniskUpload = failedFilesRetryCountLemnisk.get(fileName) != null ? failedFilesRetryCountLemnisk.get(fileName) : 0;
        int failedRetryCountClientUpload = failedFilesRetryCountClient.get(fileName) != null ? failedFilesRetryCountClient.get(fileName) : 0;

        // Failed client upload Threshold, but uploaded to Lemnisk
        if (failedRetryCountClientUpload > S3FileProcessingConstants.MAX_RETRIES_S3_UPLOAD && alreadyUploadedFilesLemnisk.get(fileName) != null) {
            failedFilesRetryCountClient.remove(fileName);
            canDelete = true;
        }

        // Failed Lemnisk upload retry Threshold, but uploaded to client
        if (failedRetryCountLemniskUpload > S3FileProcessingConstants.MAX_RETRIES_S3_UPLOAD && alreadyUploadedFilesClient.get(fileName) != null) {
            failedFilesRetryCountLemnisk.remove(fileName);
            canDelete = true;
        }

        // Failed threshold for both client and lemnisk upload
        if (failedRetryCountClientUpload > S3FileProcessingConstants.MAX_RETRIES_S3_UPLOAD && failedRetryCountLemniskUpload > S3FileProcessingConstants.MAX_RETRIES_S3_UPLOAD) {
            canDelete = true;
        }

        if (canDelete) {
            LOGGER.info("File deleted after failed retries: {}", file);
            file.delete();
        }

    }

    public boolean uploadToS3Lemnisk(File file, String bucketName) {

        boolean isFiledUploadedToLemniskBucket = false;
        boolean isalreadyUploadedFilesLemnisk = alreadyUploadedFilesLemnisk.get(file.toString()) != null;

        if (isalreadyUploadedFilesLemnisk) {
            // File already exists no need to be uploaded again
            LOGGER.info("File already exists in Lemnisk Folder Skipping : {} " + file.toString());
            isFiledUploadedToLemniskBucket = true;
            return isFiledUploadedToLemniskBucket;
        }

        if (!isalreadyUploadedFilesLemnisk) {

            isFiledUploadedToLemniskBucket = S3Util.uploadToS3Lemnisk(bucketName,file.getPath(),getS3KeyForFileNameLemnisk(file.getPath()));
            if (isFiledUploadedToLemniskBucket) {
                LOGGER.info("File Uploaded to Lemnisk S3 Bucket: {} File : {}", bucketName, file);
                updateMetrics("Lemnisk", file.getPath());
                FileUtils.copyFile(file, FileProcessingHelper.getDirectoryNameByProcessingStage(S3FileProcessingConstants.UPLOADED_LEMNISK) + file.getName());
            } else {
                LOGGER.warn("Failed to upload file to client S3 Bucket: {} : File {} ", bucketName, file);
                processFailedUploads(file, S3FileProcessingConstants.UPLOADED_LEMNISK);
            }
        }

        return isFiledUploadedToLemniskBucket;

    }


    public Boolean uploadToClientS3(File file, String bucketName, String arn, String region) {

        boolean isFileUploadedToClientBucket = false;
        boolean isalreadyUploadedFilesClient = alreadyUploadedFilesClient.get(file.toString()) != null;

        if (isalreadyUploadedFilesClient) {
            // File already exists no need to be uploaded again
            LOGGER.info("File already exists in Client Folder Skipping : {} ", file);
            isFileUploadedToClientBucket = true;
            return isFileUploadedToClientBucket;
        }

        if (!isalreadyUploadedFilesClient) {

            String s3Key = getS3KeyForFileNameClient(file.getPath());

            isFileUploadedToClientBucket = S3Util.uploadToS3Client(bucketName,file.getPath(), s3Key, arn , region );

            if (isFileUploadedToClientBucket) {
                LOGGER.info("File Uploaded to Client S3 Bucket: {} File : {}", bucketName, file);
                updateMetrics("Client", file.getPath());
                FileUtils.copyFile(file, FileProcessingHelper.getDirectoryNameByProcessingStage(S3FileProcessingConstants.UPLOADED_CLIENT) + file.getName());
            } else {
                LOGGER.warn("Failed to upload file to client S3 Bucket: {} : File {} ", bucketName, file);
                processFailedUploads(file, S3FileProcessingConstants.UPLOADED_CLIENT);
            }
        }

        return isFileUploadedToClientBucket;

    }


    /*
         Sample Completed Log file directory structure
         /disk1/logs/destination-sender/PROCESSED/1/1/2022/5/28/10/logfile.gz
         [0]    [1]   [2]                [3]     [4][5][6][7][8][9]

         [0] -> Root folder /disk
         [1],[2] -> Sub folders to arrange logs
         [3] -> Stage folders to seperate processing,processed files
         [4] -> Destination Id
         [5] -> Source Id
         [6] -> Year
         [7] -> Month
         [8] -> Day
         [9] -> Hour
         [10] -> File
     */

    public static String getS3KeyForFileNameClient(String fileName){

        String[] fileParts = Arrays.stream(fileName.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        if (fileParts.length != 11){
            throw new RuntimeException("Invalid Log File Name:" + fileName);
        }

        StringBuilder s3Key = new StringBuilder();

        s3Key.append("lemnisk-logs/").
                    append(fileParts[5])
                    .append("/")
                    .append(fileParts[6])
                    .append("/")
                    .append(fileParts[7])
                    .append("/")
                    .append(fileParts[8])
                    .append("/")
                    .append(fileParts[10]);

        return s3Key.toString();

    }

    public static String getS3KeyForFileNameLemnisk(String fileName){

        String[] fileParts = Arrays.stream(fileName.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        if (fileParts.length != 11){
            throw new RuntimeException("Invalid Log File Name:" + fileName);
        }

        StringBuilder s3Key = new StringBuilder();

        s3Key.append("lemnisk-logs/").
                append(fileParts[4])
                .append("/")
                .append(fileParts[5])
                .append("/")
                .append(fileParts[6])
                .append("/")
                .append(fileParts[7])
                .append("/")
                .append(fileParts[8])
                .append("/")
                .append(fileParts[10]);

        return s3Key.toString();

    }


    private String fetchBucketConfigByDestination(String fileName) {

        String[] fileParts = Arrays.stream(fileName.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        if (fileParts.length != 11){
            throw new RuntimeException("Invalid Log File Name:" + fileName);
        }

        int destinationId = Integer.parseInt(fileParts[4]);
        CDPDestinationInstance instance  = destinationInstanceService.getDestinationInstanceEntity(destinationId);

        if (instance == null){
            throw new RuntimeException("Invalid file! Could not get related destination instance: " + fileName);
        }

        return instance.getConfigJson();

    }

    private void updateMetrics(String uploadType, String fileName){

        String[] fileParts = Arrays.stream(fileName.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        if (fileParts.length != 11){
            throw new RuntimeException("Invalid Log File Name:" + fileName);
        }

        String destinationId = fileParts[4]; // destinationId
        String sourceId = fileParts[5]; // sourceId;
        eventMonitoring.addDestUploadResponseMetrics(sourceId,destinationId,uploadType,fileName);
    }

}