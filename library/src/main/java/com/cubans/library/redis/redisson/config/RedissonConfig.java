package com.cubans.library.redis.redisson.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.ReplicatedServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RedissonConfig {

    @Value("${redisson.server.address:}")
    protected String redisServerAddress;
    @Value("${redisson.server.password:}")
    private String redisServerPassword;
    @Value("${redisson.replicate.mode:false}")
    private Boolean redissonReplicateMode;
    @Value("${redisson.retryAttempts:10}")
    private int retryAttempts;


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        if (this.redissonReplicateMode) {
            String[] addresses = this.redisServerAddress.split(",");

            ReplicatedServersConfig cfg = config.useReplicatedServers()
                .setScanInterval(2000)
                .addNodeAddress(addresses)
                .setRetryAttempts(this.retryAttempts)
                .setReadMode(ReadMode.MASTER);

            if (StringUtils.isNotBlank(this.redisServerPassword)) {
                cfg.setPassword(this.redisServerPassword);
            }

        } else {

            SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(this.redisServerAddress)
                .setDnsMonitoringInterval(2000L)
                .setRetryAttempts(this.retryAttempts)
                .setRetryInterval(3000)
                .setPingConnectionInterval(2000);

            if (StringUtils.isNotBlank(this.redisServerPassword)) {
                singleServerConfig.setPassword(this.redisServerPassword);
            }
        }

        return Redisson.create(config);
    }
}
