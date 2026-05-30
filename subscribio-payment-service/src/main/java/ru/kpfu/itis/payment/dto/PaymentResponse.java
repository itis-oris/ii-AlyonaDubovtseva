package ru.kpfu.itis.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResponse {
    private boolean success;
    private String transactionId;
    private String message;
}