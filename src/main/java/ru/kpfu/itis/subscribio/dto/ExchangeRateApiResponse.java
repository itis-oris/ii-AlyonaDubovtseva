package ru.kpfu.itis.subscribio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExchangeRateApiResponse {

    private String result;

    @JsonProperty("conversion_result")
    private BigDecimal conversionResult;
}

