package com.cubans.redis.configuration;

import com.cubans.library.config.BaseAppConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {Constant.BASE_PACKAGE, Constant.REDISSON_CONFIG_PACKAGE})
public class AppConfig extends BaseAppConfig {

}
