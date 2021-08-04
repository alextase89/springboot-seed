package com.cubans.awss3.configuration;

import com.cubans.library.config.BaseAppConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = Constant.BASE_PACKAGE)
public class AppConfig extends BaseAppConfig {

}
