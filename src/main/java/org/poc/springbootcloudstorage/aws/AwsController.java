package org.poc.springbootcloudstorage.aws;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
public class AwsController {

    private final S3Client s3Client;

    public AwsController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s", name);
    }

    @GetMapping("/files/aws/{fileName}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable(name = "fileName") String key) throws IOException {
        String bucketName = "aws-da-docs";

        GetObjectRequest s3Request = s3GetObjectRequest(bucketName, key);
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(s3Request);
        byte[] bytes = response.readAllBytes();

        return ResponseEntity
                .ok()
                .headers(contentDisposition(key))
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }

    private GetObjectRequest s3GetObjectRequest(String bucketName, String key) {
        return GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .build();
    }

    private HttpHeaders contentDisposition(String fileName) {
        ContentDisposition contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(fileName)
                .build();
        var headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return headers;
    }

}
