package com.sharp.sharpshap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;

@SpringBootApplication
@EnableCaching
public class SharpshapApplication {
    public static void main(String[] args) {
        SpringApplication.run(SharpshapApplication.class, args);
    }
}
