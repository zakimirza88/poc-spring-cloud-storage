package org.poc.springbootcloudstorage.oci;

import java.io.IOException;

import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.cloud.spring.autoconfigure.core.CredentialsProperties;
import com.oracle.cloud.spring.autoconfigure.core.CredentialsProvider;

public final class CustomOciCredentialsProvider extends CredentialsProvider {

    private final SimpleAuthenticationDetailsProvider authenticationDetailsProvider;

    public CustomOciCredentialsProvider(CredentialsProperties properties) throws IOException {
        super(properties);
        this.authenticationDetailsProvider = simpleAuthenticationDetailsProvider(properties);
    }

    @Override
    public SimpleAuthenticationDetailsProvider getAuthenticationDetailsProvider() {
        return authenticationDetailsProvider;
    }

    private SimpleAuthenticationDetailsProvider simpleAuthenticationDetailsProvider(CredentialsProperties properties) {
        return SimpleAuthenticationDetailsProvider.builder()
                .userId(properties.getUserId())
                .tenantId(properties.getTenantId())
                .fingerprint(properties.getFingerprint())
                .privateKeySupplier(ociCustomKeySupplier(properties.getPrivateKey()))
                .passPhrase(properties.getPassPhrase())
                .build();
    }

    private OciCustomKeySupplier ociCustomKeySupplier(String privateKey) {
        return new OciCustomKeySupplier(privateKey);
    }

}
