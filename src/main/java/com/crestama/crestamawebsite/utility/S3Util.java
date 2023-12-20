package com.crestama.crestamawebsite.utility;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.*;

public class S3Util {
    private static final String BUCKET = "elasticbeanstalk-us-east-1-739285003742";
    private static final String accessKey = "AKIA2YIGR5XPN4X656J5";
    private static final String secretAccessKey = "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";
    public static final String imageFolderURL =
            "https://elasticbeanstalk-us-east-1-739285003742.s3.amazonaws.com/gallery-photos/";

    public static final String reportFolderURL =
            "https://elasticbeanstalk-us-east-1-739285003742.s3.amazonaws.com/sales-reports/";

    private static final S3Client s3Client = S3Client.builder().credentialsProvider(
                        () -> AwsBasicCredentials.create(accessKey, secretAccessKey)
                )
                .region(Region.US_EAST_1).build();

    public static void uploadImage(String fileName, InputStream inputStream) throws IOException {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(BUCKET).key(fileName)
                    .acl("public-read")
                    .contentType("image/*")
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void uploadReport(String fileName, InputStream inputStream) {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(BUCKET).key(fileName)
                    .contentType("xlsx")
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static byte[] downloadReport(String fileName) {
        GetObjectRequest request = GetObjectRequest.builder().bucket(BUCKET).key(fileName).build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(request);

        return objectBytes.asByteArray();
    }
}