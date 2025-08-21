package com.sharp.sharpshap.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyInitializer implements CommandLineRunner {
    private final CurrencyService currencyService;
    private final Logger logger = LoggerFactory.getLogger(CurrencyInitializer.class);

    @Override
    public void run(String... args) {
        logger.info("Запуск приложения Sharp");
        currencyService.initializeCurrencies();
    }
} 