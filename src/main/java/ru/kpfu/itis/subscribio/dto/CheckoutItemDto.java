package ru.kpfu.itis.subscribio.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CheckoutItemDto {
    private String title;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}

