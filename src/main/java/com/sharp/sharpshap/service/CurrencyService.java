package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.NbrbRate;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurrencyService {
    private static final Duration UPDATE_INTERVAL = Duration.ofHours(1);
    private CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<EnumCurrency> getAllCurrency() {
        return currencyRepository.findAll();
    }

    public void updateRatesIfNeeded() {
        List<EnumCurrency> currencies = this.getAllCurrency();
        currencies.stream().filter(currency -> shouldUpdate(currency)).forEach(currency -> updateRate(currency));
    }

    private boolean shouldUpdate(EnumCurrency currency) {
        return currency.getLastUpdate() == null ||
                Duration.between(currency.getLastUpdate(), LocalDateTime.now())
                        .compareTo(UPDATE_INTERVAL) > 0;
    }

    private void updateRate(EnumCurrency currency) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://api.nbrb.by")
                    .build();

            String currencyCode = currency.getDescription();
            NbrbRate rate = webClient.get()
                    .uri("/exrates/rates/" + currencyCode + "?parammode=2")
                    .retrieve()
                    .bodyToMono(NbrbRate.class)
                    .block();

            currency.setRate(rate.getCurOfficialRate());
            currency.setLastUpdate(LocalDateTime.now());
            currencyRepository.save(currency);
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении курса " + currency + ": " + e.getMessage());
        }
    }

    public void initializeCurrencies() {
        if (currencyRepository.count() == 0) {
            EnumCurrency usd = new EnumCurrency("USD");
            EnumCurrency eur = new EnumCurrency("EUR");
            currencyRepository.save(usd);
            currencyRepository.save(eur);
            updateRatesIfNeeded();
        }
    }
}
