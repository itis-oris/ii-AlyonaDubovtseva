package ru.kpfu.itis.subscribio.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CartItemDto {
    private Long cartItemId;
    private Long productId;
    private String title;
    private BigDecimal priceRub;
    private Integer quantity;
    private BigDecimal subtotalRub;
}
