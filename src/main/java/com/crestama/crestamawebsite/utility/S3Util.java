package com.crestama.crestamawebsite.utility;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class S3Util {
    private static final String BUCKET = "elasticbeanstalk-us-east-1-739285003742";
    private static final String accessKey = "AKIA2YIGR5XPN4X656J5";
    private static final String secretAccessKey = "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";
    public static final String imageFolderURL =
            "https://elasticbeanstalk-us-east-1-739285003742.s3.amazonaws.com/gallery-photos/";

    private static S3Client s3Client() {
        return S3Client.builder().credentialsProvider(
                        () -> AwsBasicCredentials.create(accessKey, secretAccessKey)
                )
                .region(Region.US_EAST_1).build();
    }

    public static void uploadFile(String fileName, InputStream inputStream) throws IOException {
        try {
            S3Client s3Client = S3Util.s3Client();

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
}
