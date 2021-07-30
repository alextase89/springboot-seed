package com.cubans.awss3.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AmazonS3Config {

    @Value("${aws.endpoint:}")
    private String endpointUrl;
    @Value("${aws.accessKey:}")
    private String accessKey;
    @Value("${aws.secretKey:}")
    private String secretKey;
    @Value("${aws.region:us-eats-1}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3ClientBuilder builder =  AmazonS3ClientBuilder.standard()
            .withRegion(Regions.fromName(this.region))
            .withCredentials(this.buildCredentials());
        if(StringUtils.isNotBlank(this.endpointUrl)) {
            builder.withEndpointConfiguration(new EndpointConfiguration(this.endpointUrl, this.region));
        }
        return builder.build();
    }

    private AWSCredentialsProvider buildCredentials() {
        if (StringUtils.isAnyBlank(this.accessKey, this.secretKey)) {
            return new DefaultAWSCredentialsProviderChain();
        }
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.accessKey, this.secretKey));
    }
}
