package org.poc.springbootcloudstorage.oci;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.cloud.spring.storage.OracleStorageResource;
import com.oracle.cloud.spring.storage.Storage;

@RestController
public class OciController {

    private final Storage storage;

    public OciController(Storage storage) {
        this.storage = storage;
    }

    @GetMapping("/files/oci/{fileName}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable(name = "fileName") String key) throws IOException {
        String bucketName = "oci-da-docs";

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
