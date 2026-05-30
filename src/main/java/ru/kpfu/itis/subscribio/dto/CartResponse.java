package ru.kpfu.itis.subscribio.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CartResponse {
    private List<CartItemDto> items;
    private BigDecimal totalRub;
    private Integer totalQuantity;
    private String currency;
    private BigDecimal convertedTotal;
}

