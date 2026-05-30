package ru.kpfu.itis.subscribio.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentForm {

    @NotBlank(message = "Номер карты обязателен")
    @Pattern(regexp = "\\d{16}", message = "Номер карты должен содержать 16 цифр")
    private String cardNumber;

    @NotBlank(message = "Срок действия обязателен")
    @Pattern(regexp = "\\d{2}/\\d{2}", message = "Формат срока действия: MM/YY")
    private String expiryDate;

    @NotBlank(message = "CVV обязателен")
    @Pattern(regexp = "\\d{3}", message = "CVV должен содержать 3 цифры")
    private String cvv;

    private String currency = "RUB";
}