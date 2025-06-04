package com.sharp.sharpshap;

import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.repository.CurrencyRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SharpshapApplication {
    public static void main(String[] args) {
        SpringApplication.run(SharpshapApplication.class, args);
    }
}
