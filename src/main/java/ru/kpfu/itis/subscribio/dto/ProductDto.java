package ru.kpfu.itis.subscribio.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal priceRub;
    private String imageUrl;
    private String durationLabel;
    private String categoryName;
    private boolean active;
}

