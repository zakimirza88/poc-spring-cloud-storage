package org.poc.springbootcloudstorage.oci;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oracle.cloud.spring.storage.OracleStorageResource;
import com.oracle.cloud.spring.storage.Storage;
import com.oracle.cloud.spring.storage.StorageObjectMetadata;

@RestController
public class OciController {

    private final String bucketName;
    private final Storage storage;

    public OciController(Storage storage) {
        this.bucketName = "oci-da-docs";
        this.storage = storage;
    }

    @GetMapping("/files/oci")
    public ResponseEntity<ByteArrayResource> download(@RequestParam(name = "fileName") String key) throws IOException {
        OracleStorageResource resource = storage.download(bucketName, key);
        byte[] bytes = resource.getContentAsByteArray();
        return ResponseEntity
                .ok()
                .headers(contentDisposition(key))
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
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
