package com.anuragroy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig{

    //Add "springfox-swagger2" Maven Dependency
    //Add "springfox-swagger-UI" Maven Dependency

    @Bean
    public Docket swaggerConfiguration(){
        //return a preapared Docket Instance
        return new Docket(DocumentationType.SWAGGER_2)
                .select()                              //for selecting the properties
                .paths(PathSelectors.ant("/login/*"))    //for showing only developer made api's - here, configure all controller's request-mapping with /api
                .apis(RequestHandlerSelectors.basePackage("com.anuragroy"))  //for showing documentaion for developer's base package and nothing from spring's default apis
                .build()                                                     //for building the docket
                .apiInfo(apiDetails()); 					//for customizing swagger's default Api Info
    }

    //Project Details
    private ApiInfo apiDetails() {
        return new ApiInfo(
                "Asset Management",
                "Asset Management Web Application",
                "1.0",			//version
                "Licensed under A2mee",		//license
                new springfox.documentation.service.Contact("Anurag Roy", "http://www.anuragroy.com/", "anurag.roy@anuragroy.com"),
                "API License",
                "http://www.anuragroy.com/",
                Collections.emptyList());
    }
}