package ru.kpfu.itis.subscribio.service;

import java.math.BigDecimal;

public interface CurrencyService {
    BigDecimal convertRubTo(String targetCurrency, BigDecimal amountRub);
}