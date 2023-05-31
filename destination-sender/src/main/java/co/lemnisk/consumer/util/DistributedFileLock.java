package co.lemnisk.consumer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DistributedFileLock {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedFileLock.class);

    public static boolean acquire(String directory){
        if (!isLockAcquired(directory)){
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream( directory + "lock" ), StandardCharsets.UTF_16))) {
                writer.write("Lock Acquired");
                return true;
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                LOGGER.warn("Directory not created yet. Skipping.");
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isLockAcquired(String directory){

        File lockFile = new File(directory + "lock");
        if(lockFile.exists() && !lockFile.isDirectory()) {

            if ( ! FileUtils.checkIfFileModifiedInXMinutes(lockFile,10)){
                // File has not been modified in the last 10 minutes remove lock
                releaseLock(directory);
                return false;
            }

            return true;
        }
        else {
            return false;
        }
    }

    public static boolean releaseLock(String directory){

        LOGGER.info("Releasing lock for directory {}",directory);

        File lockFile = new File(directory + "lock");
        if (lockFile.exists()){
            if (lockFile.delete()) {
                LOGGER.info("Lock File was deleted: {} lock" , directory);
                return true;
            }
        }

        LOGGER.warn("Could not delete lock file: {} lock", directory );
        return false;
    }

}
