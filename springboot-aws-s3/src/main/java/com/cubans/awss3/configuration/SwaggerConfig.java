package com.cubans.awss3.configuration;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.service.title}")
    private String title ;
    @Value("${swagger.service.description}")
    private String description ;
    @Value("${swagger.service.version}")
    private String version ;
    @Value("${swagger.service.termsOfServiceUrl}")
    private String termsOfServiceUrl ;
    @Value("${swagger.service.contact.name}")
    private String contactName ;
    @Value("${swagger.service.contact.website:}")
    private String contactWebsite ;
    @Value("${swagger.service.contact.email}")
    private String contactEmail ;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(Constant.BASE_PACKAGE))
            .build()
            .apiInfo(new ApiInfo(title,
                description,
                version,
                termsOfServiceUrl,
                new Contact(contactName, contactWebsite,contactEmail),
                null,
                null,
                Collections.emptyList()));
    }
}