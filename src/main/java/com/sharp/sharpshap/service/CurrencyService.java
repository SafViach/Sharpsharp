package com.sharp.sharpshap.service;

import ch.qos.logback.core.util.TimeUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sharp.sharpshap.dto.CurrencyResponseDTO;
import com.sharp.sharpshap.dto.NbrbRate;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.exceptions.CurrencyNotFoundException;
import com.sharp.sharpshap.repository.CurrencyRepository;
import io.netty.channel.ChannelHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "currency")
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private static final Duration UPDATE_INTERVAL = Duration.ofHours(1);
    private static final String BASE_CURRENCY = "BYN";

    private final Logger logger = LoggerFactory.getLogger(CurrencyService.class);
    private final CurrencyRepository currencyRepository;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.nbrb.by")
            .build();

    @Cacheable(cacheNames ="getAllCurrencyDTO")
    public List<CurrencyResponseDTO> getAllCurrencyDTO() {
        List<CurrencyResponseDTO> responseDTO = currencyRepository.findAll()
                .stream()
                .map(currency -> new CurrencyResponseDTO(
                        currency.getId(),
                        currency.getDescription(),
                        currency.getRate(),
                        currency.getLastUpdate()))
                .collect(Collectors.toList());
        return responseDTO;
    }

    public List<EnumCurrency> getAllCurrency() {
        return currencyRepository.findAll();
    }
    public EnumCurrency getByDescription(String description){
        return currencyRepository.findByDescription(description).orElseThrow(()->
                new CurrencyNotFoundException("Курс валюты по имнени: " + description + " в базе не найден"));
    }

    public EnumCurrency getById(UUID uuidCurrency) {
        return currencyRepository.findById(uuidCurrency).orElseThrow(() -> new CurrencyNotFoundException(
                "Валюта по uuid не найдена"
        ));
    }
    public CurrencyResponseDTO getDtoById(UUID uuidCurrency) {
        EnumCurrency currency = getById(uuidCurrency);
        CurrencyResponseDTO currencyResponseDTO = new CurrencyResponseDTO();
        currencyResponseDTO.setId(currency.getId());
        currencyResponseDTO.setDescription(currency.getDescription());
        currencyResponseDTO.setRate(currency.getRate());
        currencyResponseDTO.setLastUpdate(currency.getLastUpdate());
        return currencyResponseDTO;
    }

    public void updateRatesIfNeeded() {
        logger.info("CurrencyService: ---shouldUpdate ");
        List<EnumCurrency> currencies = getAllCurrency();

        for (EnumCurrency currency : currencies) {
            if (BASE_CURRENCY.equalsIgnoreCase(currency.getDescription())) continue;
            logger.info("CurrencyService: ---shouldUpdate проверка на обновление" + currency.getDescription());
            if (shouldUpdate(currency)){
                logger.info("CurrencyService: ---shouldUpdate обновляем" + currency.getDescription());
                updateRate(currency);
            }
        }
    }

    private boolean shouldUpdate(EnumCurrency currency) {
        logger.info("CurrencyService: ---shouldUpdate проверка время срока обновления курс валюты :"
                + currency.getDescription());
        return currency.getLastUpdate() == null ||
                Duration.between(currency.getLastUpdate(), LocalDateTime.now())
                        .compareTo(UPDATE_INTERVAL) > 0;
    }

    private void updateRate(EnumCurrency currency) {
        logger.info("CurrencyService: ---updateRate запрос к API НБРБ пр коду валюты :" +currency.getDescription());
        try {
            NbrbRate rate = webClient.get()
                    .uri("/exrates/rates/" + currency.getDescription() + "?parammode=2")
                    .retrieve()
                    .bodyToMono(NbrbRate.class)
                    .block();
            logger.info("CurrencyService: ---updateRate получили от API НБРБ по коду валюты :"
                    + currency.getDescription() + " = " + rate.getCurOfficialRate());
            if (rate != null && rate.getCurOfficialRate() != null) {
                currency.setRate(rate.getCurOfficialRate());
                currency.setLastUpdate(LocalDateTime.now());
                logger.info("CurrencyService: ---updateRate обновляем " + currency.getDescription());
                currencyRepository.save(currency);
            }
        } catch (Exception e) {
            logger.error("Ошибка при обновлении курса " + currency.getDescription() + ": " + e.getMessage());
        }
    }

    public void initializeCurrencies() {
        long count = currencyRepository.count();

        logger.info("CurrencyService: ---initializeCurrencies Инициализация курс валют");
        logger.info("CurrencyService: ---initializeCurrencies Колличество записей в базе курс = " + count);

        if (count == 0) {
            logger.info("CurrencyService: ---initializeCurrencies база пуста : " +
                    "Создаём BYN , USD , EUR");
            saveInBaseCurrencyIfMissing();
            saveCurrency("USD");
            saveCurrency("EUR");
        }
        updateRatesIfNeeded();
    }

    private void saveInBaseCurrencyIfMissing() {
        logger.info("CurrencyService: ---Проверяем есть ли " + BASE_CURRENCY + " в базе");
        currencyRepository.findByDescription(BASE_CURRENCY)
                .or(() -> Optional.of(currencyRepository.save(getBaseCurrency())))
                .ifPresent(baseCurrency -> {
                    if (baseCurrency.getRate() == null || baseCurrency.getRate().compareTo(BigDecimal.ONE) != 0) {
                        baseCurrency.setRate(BigDecimal.ONE);
                        baseCurrency.setLastUpdate(LocalDateTime.now());
                        currencyRepository.save(baseCurrency);
                    }
                });
    }

    private EnumCurrency getBaseCurrency() {
        logger.info("CurrencyService: ---getBaseCurrency() Создаю обьект EnumCurrency для " + BASE_CURRENCY);

        EnumCurrency baseCurrency = new EnumCurrency();
        baseCurrency.setDescription(BASE_CURRENCY);
        baseCurrency.setRate(BigDecimal.ONE);
        baseCurrency.setLastUpdate(LocalDateTime.now());

        logger.info("CurrencyService: ---getBaseCurrency() Добавляю в базу " + baseCurrency);
        return baseCurrency;
    }

    private void saveCurrency(String description) {
        logger.info("CurrencyService: ---saveCurrency проверяем на наличие в базе " + description);
        currencyRepository.findByDescription(description).orElseGet(() ->
        {
            EnumCurrency currency = new EnumCurrency();
            currency.setDescription(description);
            logger.info("сохраняем в базу " + currency);
            return currencyRepository.save(currency);
        });
    }
}
