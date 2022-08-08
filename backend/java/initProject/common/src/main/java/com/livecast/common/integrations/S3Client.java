package com.livecast.common.integrations;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.livecast.common.entity.Attachment;
import com.livecast.common.entity.S3PresignedUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

@Service
public class S3Client {
    private AmazonS3 s3client;

    @Value("${amazonProperties.region}")
    private String region;

    @Value("${amazonProperties.uploadsBucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Value("${amazonProperties.signatureExpirationSeconds}")
    private long signatureExpirationSeconds;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3Client.builder()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    public S3PresignedUrl generateSignedUrl(String subPath) {
        return generateSignedUrl(subPath, UUID.randomUUID().toString(), HttpMethod.PUT, null);
    }

    public Attachment generateSignedUrl(Attachment attachment) {
        S3PresignedUrl s3PresignedUrl = generateSignedUrl(attachment.getSubPath(),
                attachment.getId(),
                HttpMethod.GET,
                attachment.getName());
        attachment.setPresignedUrl(s3PresignedUrl.getPresignedUrl());
        return attachment;
    }

    public S3PresignedUrl generateSignedUrl(String subPath, String objectId, HttpMethod method, String fileName) {
        String objectKey = subPath + "/" + objectId;

        try {
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime() + (signatureExpirationSeconds * 1000);
            expiration.setTime(expTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
                    .withMethod(method)
                    .withExpiration(expiration);

            if (fileName != null) {
                ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
                responseHeaders.setContentDisposition("inline; filename =\"" + fileName + "\"");
                responseHeaders.withContentType(URLConnection.guessContentTypeFromName(fileName));
                generatePresignedUrlRequest.withResponseHeaders(responseHeaders);
            }

            URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

            return S3PresignedUrl.builder()
                    .id(objectId)
                    .subPath(subPath)
                    .presignedUrl(url.toString())
                    .build();
        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String subPath, String objectId) {
        String objectKey = subPath + "/" + objectId;
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName,
                objectKey);
        s3client.deleteObject(deleteObjectRequest);
    }

    public static class S3SubPaths {
        public static final String USER_PROFILE_PICTURES_SUB_PATH = "users/profile_pictures";
    }
}