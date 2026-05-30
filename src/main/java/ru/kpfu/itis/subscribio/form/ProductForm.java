package ru.kpfu.itis.subscribio.form;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductForm {

    @NotBlank(message = "Название обязательно")
    private String title;

    @NotBlank(message = "Описание обязательно")
    private String description;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "1.00", message = "Цена должна быть больше 0")
    private BigDecimal priceRub;

    private String imageUrl;

    @NotBlank(message = "Срок подписки обязателен")
    private String durationLabel;

    @NotNull(message = "Категория обязательна")
    private Long categoryId;

    private boolean active = true;
}