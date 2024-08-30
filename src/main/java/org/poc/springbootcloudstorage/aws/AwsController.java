package org.poc.springbootcloudstorage.aws;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RestController
public class AwsController {

    private final String bucketName;
    private final S3Client s3Client;

    public AwsController(S3Client s3Client) {
        this.bucketName = "aws-da-docs";
        this.s3Client = s3Client;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s", name);
    }

    @GetMapping("/files/aws")
    public ResponseEntity<ByteArrayResource> download(@RequestParam(name = "fileName") String key) throws IOException {
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

    @PostMapping("/files/aws")
    public ResponseEntity<?> upload(@RequestPart(name = "file") MultipartFile file,
            @RequestParam(name = "fileName", required = false) String key) throws IOException {
        PutObjectRequest putObjectRequest = s3PutObjectRequest(bucketName, StringUtils.hasText(key) ? key : file.getOriginalFilename());
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/files/aws")
    public ResponseEntity<?> delete(@RequestParam(name = "fileName") String key) {
        DeleteObjectRequest deleteObjectRequest = s3DeleteObjectRequest(bucketName, key);
        s3Client.deleteObject(deleteObjectRequest);
        return ResponseEntity.ok().build();
    }

    private GetObjectRequest s3GetObjectRequest(String bucketName, String key) {
        return GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .build();
    }

    private PutObjectRequest s3PutObjectRequest(String bucketName, String key) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
    }

    private DeleteObjectRequest s3DeleteObjectRequest(String bucketName, String key) {
        return DeleteObjectRequest.builder()
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
