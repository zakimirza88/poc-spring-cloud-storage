package org.poc.springbootcloudstorage.aws;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
public class Controller {

    private final S3Client s3Client;

    public Controller(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s", name);
    }

    @GetMapping("/files/aws")
    public ResponseEntity<ByteArrayResource> test() throws IOException {
        String bucketName = "demo-da-docs";
        String fileName = "lorem IPSUM.pdf";
        GetObjectRequest s3Request = s3GetObjectRequest(bucketName, fileName);
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(s3Request);
        byte[] bytes = response.readAllBytes();
        return ResponseEntity
                .ok()
                .headers(contentDisposition(fileName))
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }

    private GetObjectRequest s3GetObjectRequest(String bucketName, String fileName) {
        return GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(fileName)
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
