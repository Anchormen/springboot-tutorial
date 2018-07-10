package nl.anchormen.usermanager;

import java.util.Arrays;
import java.util.Collections;

import javax.validation.Validator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class UserApp
{

    public static void main(String[] args)
    {
        SpringApplication.run(UserApp.class, args);
    }
    
    /**
     * Enable javax.validation
     * @return
     */
    @Bean
    public Validator localValidatorFactoryBean()
    {
        return new LocalValidatorFactoryBean();
    }
    
    /**
     * Swagger config
     * 
     * endpoints:
     * http://localhost:8080/v2/api-docs 
     * http://localhost:8080/swagger-ui.html
     *
     */
    @Bean
    public Docket api()
    {
        ApiInfo apiInfo = new ApiInfo("User Manager API", "API for managing users", "", "",
            new Contact("pieter", "", ""), "", "", Collections.emptyList());
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            //exclude default spring error page
            .paths((s) -> !s.equals("/error")) 
            .build()
            .apiInfo(apiInfo)
            .securitySchemes(Arrays.asList(new BasicAuth("basic")));
    }
}
