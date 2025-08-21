package com.sharp.sharpshap.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {
    //@Cacheable-Кэширует результат метода. Если данные есть — не вызывает метод.
    //@CachePut-Всегда выполняет метод и обновляет кэш.
    //@CacheEvict-Удаляет данные из кэша (по ключу или полностью).
    //@Caching-Группировка нескольких аннотаций кэширования.
    //@CacheConfig-Настройки кэша на уровне класса (например, имя по умолчанию).
    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofMinutes(60))
                .recordStats());
        return cacheManager;
    }
}
