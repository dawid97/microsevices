package com.javasolution.app.userservice;

import com.javasolution.app.userservice.security.AuthInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.javasolution.app.userservice.controllers"))
				.paths(PathSelectors.any())
				.build().apiInfo(new ApiInfo("User Service Api Documentation",
						"Documentation automatically generated",
						"1.0",
						null,
						new Contact("Dawid Ulfik", "https://www.facebook.com/dawid.ulfik.3", "dawulf97@gmail.com"),
						null,
						null)
				);
	}

	@Bean
	AuthInterceptor authFeign() {
		return new AuthInterceptor();
	}
}
