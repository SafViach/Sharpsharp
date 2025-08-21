package com.sharp.sharpshap.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@EnableScheduling
@Component
@RequiredArgsConstructor
public class CurrencyScheduler {
    private final CurrencyService currencyService;
    private final Logger logger = LoggerFactory.getLogger(CurrencyScheduler.class);

    @Scheduled(fixedRate = 600000) // Автозапуск каждые 10 мин
    public void updateCurrencyRates() {
        logger.info("CurrencyScheduler: ---updateCurrencyRates Автообновление курс валют");
        currencyService.updateRatesIfNeeded();
    }
}