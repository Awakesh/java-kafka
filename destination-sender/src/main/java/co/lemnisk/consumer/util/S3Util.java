package co.lemnisk.consumer.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

import java.io.File;


/**
 * Utility to interact with AWS S3
 */

public class S3Util {


    private static final Logger LOGGER = LoggerFactory.getLogger(S3Util.class);

        public static boolean checkBucketExists(String bucketName, S3Client s3Client ){

            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder().bucket(bucketName).build();
            try{
                s3Client.headBucket(headBucketRequest);
                return true;
            }
            catch (NoSuchBucketException exception){
                return false;
            }
        }

        public static boolean uploadToS3Lemnisk(String bucketName,String filePath, String s3Key){

            Region region = Region.AWS_GLOBAL;
            S3Client s3Client = S3Client.builder()
                    .region(region)
                    .build();
            try{
                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(bucketName).key(s3Key).build();
                s3Client.putObject(request, RequestBody.fromFile(new File(filePath)));
                LOGGER.info("File successfully uploaded" + filePath);
                return true;
            }
            catch (Exception e){
                LOGGER.error("Exception occured while uploading file {} exception {}", filePath, e.getMessage());
                return false;
            }
        }

        public static boolean uploadToS3Client(String bucketName,String filePath, String s3Key, String roleArn, String region){

            String roleSessionName = "Session-Lemnisk";
            AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder().roleSessionName(roleSessionName).roleArn(roleArn).build();
            StsClient stsClient = StsClient.builder().region(Region.of(region)).build();
            StsAssumeRoleCredentialsProvider stsAssumeRoleCredentialsProvider = StsAssumeRoleCredentialsProvider.builder().stsClient(stsClient).refreshRequest(assumeRoleRequest).build();
            S3Client s3Client = S3Client.builder().credentialsProvider(stsAssumeRoleCredentialsProvider).region(Region.of(region))
                    .build();

            try{
                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(bucketName).key(s3Key).build();
                s3Client.putObject(request, RequestBody.fromFile(new File(filePath)));
                LOGGER.info("File successfully uploaded" + filePath);
                return true;
            }
            catch (Exception e){
                LOGGER.error("Exception occured while uploading file {} exception {}", filePath, e.getMessage());
                return false;
            }

        }

}
