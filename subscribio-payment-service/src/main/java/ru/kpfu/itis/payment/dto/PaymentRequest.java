package ru.kpfu.itis.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequest {
    private BigDecimal amount;
    private String currency;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}