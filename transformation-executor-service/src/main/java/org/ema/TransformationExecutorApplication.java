package org.ema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TransformationExecutorApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransformationExecutorApplication.class, args);
    }
}