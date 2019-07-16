package com.cai.smith.videogameapi.utility;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.cai.smith.videogameapi.AwsConfig;
import com.cai.smith.videogameapi.VideogameApiApplication;
import com.cai.smith.videogameapi.exception.FileDownloaderException;
import com.cai.smith.videogameapi.model.Developers;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileDownloader {

    private static final Logger logger =
            LoggerFactory.getLogger(VideogameApiApplication.APPLICATION_NAME_SPACE);

    @Autowired
    private AwsConfig awsConfig;

    public Developers getApprovedDevelopers() throws FileDownloaderException {
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsConfig.getAccessKey(), awsConfig.getSecretAccessKey());

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(awsConfig.getRegion()).withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .build();

            String bucketName = awsConfig.getBucketName();
            String fileNameInS3 = awsConfig.getFileName();

            S3Object fullObject;

            logger.info("Attempting to download file from S3");
            fullObject = s3Client.getObject(new GetObjectRequest(bucketName, fileNameInS3));

            logger.info("File download complete");

            InputStream is = fullObject.getObjectContent();

            logger.info("Attempting to map JSON to Developers object");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            return objectMapper.readValue(is, Developers.class);

        } catch (IOException | AmazonS3Exception e) {
            logger.error("Failed to download file from S3", e);
            throw new FileDownloaderException(e);
        } catch (SdkClientException sdkEx) {
            logger.error("SDK exception thrown when attempting to download file");
            throw new FileDownloaderException(sdkEx);
        }
    }
}
