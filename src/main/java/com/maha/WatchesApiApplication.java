package com.maha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class WatchesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatchesApiApplication.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any()).build().apiInfo(apiInfo()).useDefaultResponseMessages(false);
    }

    @Bean
    public ApiInfo apiInfo() {
        final ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title("Watches Api").version("1.0").license("(C) Copyright - For maha coding challenge")
                .description("List of all the APIs of Watches App through Swagger UI");
        return builder.build();
    }

}
