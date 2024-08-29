package org.poc.springbootcloudstorage.oci;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oracle.cloud.spring.autoconfigure.core.CredentialsProperties;
import com.oracle.cloud.spring.autoconfigure.core.CredentialsProvider;

@Configuration
public class OciConfig {

    @Bean
    public CredentialsProvider credentialsProvider(CredentialsProperties credentialsProperties) throws IOException {
        return new CustomOciCredentialsProvider(credentialsProperties);
    }

}
