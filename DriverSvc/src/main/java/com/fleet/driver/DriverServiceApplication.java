package com.fleet.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Driver Service API",
        version = "1.0",
        description = "API documentation for Driver Management Service"
    )
)
@EnableFeignClients
public class DriverServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DriverServiceApplication.class, args);
    }
} 