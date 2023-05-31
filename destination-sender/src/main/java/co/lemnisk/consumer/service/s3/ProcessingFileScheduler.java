package co.lemnisk.consumer.service.s3;


import co.lemnisk.consumer.util.DistributedFileLock;
import co.lemnisk.consumer.util.FileProcessingHelper;
import co.lemnisk.consumer.util.FileUtils;
import co.lemnisk.consumer.util.S3FileProcessingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EnableRetry
@EnableScheduling
@Service
public class ProcessingFileScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingFileScheduler.class);

    @Scheduled(cron = "0 0/30 * * * *")
    @Retryable(value = Throwable.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void processFilesInProcessing() throws IOException {

        List<File> filesToBeProcessed = new ArrayList<>();
        String PROCESSING_DIRECTORY = FileProcessingHelper.getDirectoryNameByProcessingStage(S3FileProcessingConstants.FILE_PROCESSING);

        FileUtils.listf(PROCESSING_DIRECTORY, filesToBeProcessed);

        // Acquire a lock on the processed directory so two pods cannot
        // access the file at the same time.
        if (DistributedFileLock.acquire(PROCESSING_DIRECTORY)) {

            // If any incomplete files are found mark them as completed based
            // on last modified timestamp
            for (File file : filesToBeProcessed) {
                FileProcessingHelper.checkIncompleteFile(file);
            }

            // Recheck Directory
            filesToBeProcessed.clear();
            FileUtils.listf(PROCESSING_DIRECTORY, filesToBeProcessed);

            // Move completed files to PROCESSED folder
            for (File file : filesToBeProcessed) {
                if (FileProcessingHelper.isValidCompletedLogFile(file)){
                    FileProcessingHelper.moveCompletedFile(file);
                }
            }

            DistributedFileLock.releaseLock(PROCESSING_DIRECTORY);

        } else {
            LOGGER.info("Cannot acquire lock on directory: {}",PROCESSING_DIRECTORY);
        }
    }

}
