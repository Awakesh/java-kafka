package co.lemnisk.consumer.controller.test;


import co.lemnisk.consumer.util.S3FileProcessingConstants;
import co.lemnisk.consumer.util.S3Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.PrintWriter;

@RestController
public class DestinationSenderS3Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationSenderS3Controller.class);

    @PostMapping(value = "/s3/client/test", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> testClientUploads(@RequestBody ClientRequest clientRequest){

        try{

            LOGGER.info(clientRequest.toString());

            String fileName = S3FileProcessingConstants.DESTINATION_SENDER_LOG_ROOT + "/" + "test-upload-S3-file-client.txt";

            File file = new File(fileName);

            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println("This is a test upload");
            writer.close();
            boolean uploadStatus = S3Util.uploadToS3Client(clientRequest.bucketName,fileName,"lemnisk-logs/test-upload-S3-file-client.txt" , clientRequest.arn, clientRequest.region);

            if (uploadStatus){
                LOGGER.info("Uploaded to client bucket: {}  ARN: {}, KEY : {} ; region {}", clientRequest.bucketName, clientRequest.arn,"lemnisk-logs/",clientRequest.region);
                file.delete();
                return new ResponseEntity<>("File uploaded", HttpStatus.OK);
            }
            else {
                file.delete();
                LOGGER.error("Could not upload to  client bucket: {}  ARN: {}, KEY : {} ; region {}", clientRequest.bucketName, clientRequest.arn,"lemnisk-logs/",clientRequest.region);
            }


        }catch (Exception e){
            LOGGER.error(e.toString());
        }

        return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);

    }


      @PostMapping(value = "/s3/lemnisk/test", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
      public ResponseEntity<String> testClientUploads(@RequestBody LemniskRequest lemniskRequest){

          try{

              LOGGER.info(lemniskRequest.toString());

              String fileName = S3FileProcessingConstants.DESTINATION_SENDER_LOG_ROOT + "/" + "test-upload-S3-file-lemnisk.txt";

              File file = new File(fileName);

              PrintWriter writer = new PrintWriter(file, "UTF-8");
              writer.println("This is a test upload");
              writer.close();

              boolean uploadStatus = S3Util.uploadToS3Lemnisk(lemniskRequest.bucketName, fileName,"test-upload-S3-file-lemnisk.txt");

              if (uploadStatus){
                  LOGGER.info("Uploaded to Lenisk bucket: {}", lemniskRequest.bucketName);
                  file.delete();
                  return new ResponseEntity<>("File uploaded", HttpStatus.OK);
              }
              else {
                  file.delete();
                  LOGGER.error("Could not upload to  Lemnisk bucket: {}", lemniskRequest.bucketName);
              }

          }catch (Exception e){
              LOGGER.error(e.toString());
          }

          return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);

      }
}
