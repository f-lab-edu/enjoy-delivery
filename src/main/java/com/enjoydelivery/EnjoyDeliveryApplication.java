package com.enjoydelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class EnjoyDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnjoyDeliveryApplication.class, args);
    }

}
