package com.shenjiachun.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Order System Application
 */
@SpringBootApplication(scanBasePackages = "com.shenjiachun.order")
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
