package com.cubans.library.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class AmazonBaseServiceConfig {

    @Value("${aws.endpoint:}")
    protected String endpointUrl;
    @Value("${aws.access-key:}")
    protected String accessKey;
    @Value("${aws.secret-key:}")
    protected String secretKey;
    @Value("${aws.region:us-eats-1}")
    protected String region;


    protected AWSCredentialsProvider buildCredentials() {
        if (StringUtils.isAnyBlank(this.accessKey, this.secretKey)) {
            return new DefaultAWSCredentialsProviderChain();
        }
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.accessKey, this.secretKey));
    }

    protected EndpointConfiguration buildEndpointConfiguration() {
        if(!StringUtils.isAnyBlank(this.endpointUrl, this.region)) {
           return new EndpointConfiguration(this.endpointUrl, this.region);
        }
        return null;
    }
}
