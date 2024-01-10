package com.crestama.crestamawebsite.utility;

import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.ArrayList;

public class S3Util {
    @Value("${s3.bucket}")
    private static final String BUCKET = "";

    @Value("${s3.access.key}")
    private static final String accessKey = "";

    @Value("${s3.secret.access.key}")
    private static final String secretAccessKey = "";

    @Value("${s3.image.folder}")
    public static final String imageFolderURL = "";

    @Value("${s3.section.image.folder}")
    public static final String sectionImageFolderURL = "";

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

    public static void uploadSectionImage(String fileName, InputStream inputStream) throws IOException {
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

    public static void uploadReport(String fileName, ByteArrayOutputStream excel) {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(BUCKET).key(fileName).build();

            try (InputStream inputStream = new ByteArrayInputStream(excel.toByteArray())) {
                s3Client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static byte[] downloadReport(String fileName) {
        GetObjectRequest request = GetObjectRequest.builder().bucket(BUCKET).key(fileName).build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(request);

        return objectBytes.asByteArray();
    }

    public static void deleteObject(String fileName) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(BUCKET).key(fileName).build();

            s3Client.deleteObject(request);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteSectionImages(ArrayList<ObjectIdentifier> objects) {
        try {
            DeleteObjectsRequest request = DeleteObjectsRequest.builder()
                    .bucket(BUCKET)
                    .delete(Delete.builder().objects(objects).build())
                    .build();

            s3Client.deleteObjects(request);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
