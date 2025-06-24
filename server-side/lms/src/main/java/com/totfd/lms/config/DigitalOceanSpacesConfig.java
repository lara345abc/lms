package com.totfd.lms.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class DigitalOceanSpacesConfig {

    @Value("${spaces.access-key}")
    private String accessKey;

    @Value("${spaces.secret-key}")
    private String secretKey;

    @Value("${spaces.endpoint}")
    private String endpoint;

    @Value("${spaces.region}")
    private String region;

    @Autowired
    Environment environment;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(endpoint, region)
//        new AmazonS3ClientBuilder.EndpointConfiguration(environment.getProperty("spaces.region"), region)
                )
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey, secretKey)
                ))
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
