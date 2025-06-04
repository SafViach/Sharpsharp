package com.sharp.sharpshap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CurrencyInitializer implements CommandLineRunner {
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyInitializer(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public void run(String... args) {
        currencyService.initializeCurrencies();
    }
} 