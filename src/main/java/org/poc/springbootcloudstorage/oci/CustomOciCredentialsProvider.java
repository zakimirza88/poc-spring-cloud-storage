package org.poc.springbootcloudstorage.oci;

import java.io.IOException;

import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.cloud.spring.autoconfigure.core.CredentialsProperties;
import com.oracle.cloud.spring.autoconfigure.core.CredentialsProvider;

public final class CustomOciCredentialsProvider extends CredentialsProvider {

    private final SimpleAuthenticationDetailsProvider authenticationDetailsProvider;

    public CustomOciCredentialsProvider(CredentialsProperties properties,
            SimpleAuthenticationDetailsProvider authenticationDetailsProvider) throws IOException {
        super(properties);
        this.authenticationDetailsProvider = authenticationDetailsProvider;
    }

    @Override
    public SimpleAuthenticationDetailsProvider getAuthenticationDetailsProvider() {
        return authenticationDetailsProvider;
    }

}
