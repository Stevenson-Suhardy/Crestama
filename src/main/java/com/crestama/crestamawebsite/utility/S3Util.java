package com.crestama.crestamawebsite.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.ArrayList;

@Component
public class S3Util {
    @Value("${s3.bucket}")
    private String bucket;

    @Value("${s3.access.key}")
    private String accessKey;

    @Value("${s3.secret.access.key}")
    private String secretAccessKey;

    @Value("${s3.image.folder}")
    public static String imageFolderURL;

    @Value("${s3.section.image.folder}")
    public static String sectionImageFolderURL;

    private final S3Client s3Client = S3Client.builder().credentialsProvider(
                        () -> AwsBasicCredentials.create(getAccessKey(), getSecretAccessKey())
                )
                .region(Region.AP_SOUTHEAST_3).build();

    public void uploadImage(String fileName, InputStream inputStream) throws IOException {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(getBucket()).key(fileName)
                    .acl("public-read")
                    .contentType("image/*")
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void uploadSectionImage(String fileName, InputStream inputStream) throws IOException {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(getBucket()).key(fileName)
                    .acl("public-read")
                    .contentType("image/*")
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void uploadReport(String fileName, ByteArrayOutputStream excel) {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(getBucket()).key(fileName).build();

            try (InputStream inputStream = new ByteArrayInputStream(excel.toByteArray())) {
                s3Client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public byte[] downloadReport(String fileName) {
        GetObjectRequest request = GetObjectRequest.builder().bucket(getBucket()).key(fileName).build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(request);

        return objectBytes.asByteArray();
    }

    public void deleteObject(String fileName) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(getBucket()).key(fileName).build();

            s3Client.deleteObject(request);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteSectionImages(ArrayList<ObjectIdentifier> objects) {
        try {
            DeleteObjectsRequest request = DeleteObjectsRequest.builder()
                    .bucket(getBucket())
                    .delete(Delete.builder().objects(objects).build())
                    .build();

            s3Client.deleteObjects(request);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getBucket() {
        return bucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    // Set static members

    @Value("${s3.image.folder}")
    public void setImageFolderURL(String imageFolderURL) {
        S3Util.imageFolderURL = imageFolderURL;
    }

    @Value("${s3.section.image.folder}")
    public void setSectionImageFolderURL(String sectionImageFolderURL) {
        S3Util.sectionImageFolderURL = sectionImageFolderURL;
    }
}
