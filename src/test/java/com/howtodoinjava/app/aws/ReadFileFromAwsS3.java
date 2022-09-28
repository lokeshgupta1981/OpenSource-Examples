package com.howtodoinjava.app.aws;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

public class ReadFileFromAwsS3 {

  @Test
  void testReadFileFromUrl() throws IOException {
    String FILE_URL = "https://howtodoinjava-s3-bucket.s3.amazonaws.com/test.txt";
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(new URL(FILE_URL).openConnection().getInputStream()));

    String line;
    StringBuilder content = new StringBuilder();
    while ((line = bufferedReader.readLine()) != null) {
      content.append(line);
    }
    Assertions.assertEquals("Hello World!", content.toString());
  }

  //Execute this test after adding credentials file in 'USER-DIR/.aws/credentials' file
  /*@Test
  void testReadFileUsingS3Client() throws IOException {

    String bucketName = "howtodoinjava-s3-bucket";
    String key = "test.txt";
    Region region = Region.US_EAST_1;

    S3Client s3 = S3Client.builder()
        .credentialsProvider(ProfileCredentialsProvider.create())
        .region(region)
        .build();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    ResponseInputStream response = s3.getObject(getObjectRequest);

    Assertions.assertEquals("Hello World!", new String(response.readAllBytes()));
  }*/
}
