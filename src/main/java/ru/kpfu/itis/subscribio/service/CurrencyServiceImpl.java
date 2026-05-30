package ru.kpfu.itis.subscribio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.subscribio.client.ExchangeRateClient;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final ExchangeRateClient exchangeRateClient;
    @Override
    @Cacheable(value = "currency-conversions", key = "#targetCurrency + '-' + #amountRub")
    public BigDecimal convertRubTo(String targetCurrency, BigDecimal amountRub) {
        log.info("CACHE MISS: курса для {} RUB -> {} нет в кэше, обращаемся к ExchangeRate API",
                amountRub, targetCurrency);
        BigDecimal result = exchangeRateClient.convertRubTo(targetCurrency, amountRub);
        log.info("Результат конвертации сохранён в Redis: {} RUB -> {} {}",
                amountRub, result, targetCurrency);
        return result;
    }
}