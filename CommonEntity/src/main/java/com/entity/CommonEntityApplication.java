package com.entity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCaching
@EnableSwagger2
@Configuration
public class CommonEntityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonEntityApplication.class, args);
    }

    @Bean
    public Docket productApi() {

        Docket docket = new Docket(DocumentationType.SWAGGER_12).select()
                .apis(RequestHandlerSelectors.basePackage("com.entity.controller"))
                //.paths(PathSelectors.any())
                .build();

        docket.apiInfo(apiInfo());
        return docket;

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Entities service")
                .description("Entities service")
                .termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
                .version("1.0") .build(); }

}
