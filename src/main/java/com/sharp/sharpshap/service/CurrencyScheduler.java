package com.sharp.sharpshap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyScheduler {
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyScheduler(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Scheduled(fixedRate = 7200000) // 2 часа в миллисекундах
    public void updateCurrencyRates() {
        currencyService.updateRatesIfNeeded();
    }
}