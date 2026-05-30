package ru.kpfu.itis.subscribio.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentServiceResponse {
    private boolean success;
    private String transactionId;
    private String message;
}

