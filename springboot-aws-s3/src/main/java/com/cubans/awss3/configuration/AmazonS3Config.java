package com.cubans.awss3.configuration;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.cubans.library.config.AmazonBaseServiceConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AmazonS3Config extends AmazonBaseServiceConfig {

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.fromName(this.region))
            .withCredentials(this.buildCredentials());
        if (StringUtils.isNotBlank(this.endpointUrl)) {
            builder.withEndpointConfiguration(new EndpointConfiguration(this.endpointUrl, this.region));
        }
        return builder.build();
    }
}
