package ru.kpfu.itis.subscribio.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CurrencyResponse {
    private String currency;
    private BigDecimal totalRub;
    private BigDecimal convertedTotal;
}

