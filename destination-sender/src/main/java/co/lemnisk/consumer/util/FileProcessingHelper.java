package co.lemnisk.consumer.util;


import java.io.File;
import java.io.IOException;

public class FileProcessingHelper {

    public static String getDirectoryNameByProcessingStage(String processingStage){

        StringBuilder baseUrl = new StringBuilder(S3FileProcessingConstants.DESTINATION_SENDER_LOG_ROOT);

        switch (processingStage){
            case S3FileProcessingConstants.FILE_PROCESSING:
                baseUrl.append("/")
                        .append(S3FileProcessingConstants.FILE_PROCESSING)
                        .append("/");
                break;

            case S3FileProcessingConstants.FILE_PROCESSED:
                baseUrl.append("/")
                        .append(S3FileProcessingConstants.FILE_PROCESSED)
                        .append("/");
                break;

            case S3FileProcessingConstants.UPLOADED_CLIENT:
                baseUrl.append("/")
                        .append(S3FileProcessingConstants.UPLOADED_CLIENT)
                        .append("/");

                break;

            case S3FileProcessingConstants.UPLOADED_LEMNISK:
                baseUrl.append("/")
                        .append(S3FileProcessingConstants.UPLOADED_LEMNISK)
                        .append("/");

                break;

            case S3FileProcessingConstants.FAILED_UPLOAD_CLIENT:
                baseUrl.append("/")
                        .append(S3FileProcessingConstants.FAILED_UPLOAD_CLIENT)
                        .append("/");

                break;

            case S3FileProcessingConstants.FAILED_UPLOAD_LEMNISK:
                baseUrl.append("/")
                        .append(S3FileProcessingConstants.FAILED_UPLOAD_LEMNISK)
                        .append("/");

                break;
        }

        return baseUrl.toString();
    }


    public static void checkIncompleteFile(File file) throws IOException {
        if (!FileUtils.checkIfFileModifiedInXMinutes(file, 60) && file.toString().contains(".log") && !file.toString().contains("completed")) {
            String directoryPath = file.getParentFile().toString();
            File completedFile = new File(directoryPath + "/" + FileUtils.getCompletedFileNameFromOriginalFile(file));
            file.renameTo(completedFile);
        }
    }

    public static void moveCompletedFile(File file) throws IOException {
        if (file.toString().contains(".log") && file.toString().contains("completed") && !file.toString().contains(".gz")) {
            String directoryPath = file.getParentFile().toString();
            directoryPath = directoryPath.replace(S3FileProcessingConstants.FILE_PROCESSING, S3FileProcessingConstants.FILE_PROCESSED);
            FileUtils.createDirectoryIfNotExists(directoryPath);
            File completedFile = new File(directoryPath + "/" + file.getName());
            if (file.renameTo(completedFile)){

                boolean successFullyCompressed = FileUtils.compressGzip(completedFile,directoryPath + "/" + completedFile.getName()  + ".gz");
                if (successFullyCompressed)
                   completedFile.delete();

            }
        }
    }

    public static boolean isValidCompletedLogFile(File file) {
        return file.getName().contains(".log") && file.getName().contains("completed");
    }

}
