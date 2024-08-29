package org.poc.springbootcloudstorage.oci;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.util.Assert;

import com.oracle.bmc.auth.SimplePrivateKeySupplier;

public class OciCustomKeySupplier extends SimplePrivateKeySupplier {

    private final String privateKey;

    public OciCustomKeySupplier(String privateKey) {
        super(null);
        this.privateKey = privateKey;
    }

    @Override
    public InputStream get() {
        Assert.hasText(privateKey, "privateKey is required");
        return new ByteArrayInputStream(privateKey.getBytes(StandardCharsets.UTF_8));
    }

}
