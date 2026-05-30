package ru.kpfu.itis.subscribio.dto.payment;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentServiceRequest {
    private BigDecimal amount;
    private String currency;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}


