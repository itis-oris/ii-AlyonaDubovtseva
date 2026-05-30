package ru.kpfu.itis.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.payment.dto.PaymentRequest;
import ru.kpfu.itis.payment.dto.PaymentResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest request) {
        if (request.getCardNumber() == null || request.getCardNumber().length() != 16) {
            return ResponseEntity.badRequest()
                    .body(new PaymentResponse(false, null, "Некорректный номер карты"));
        }
        if (request.getCvv() == null || request.getCvv().length() != 3) {
            return ResponseEntity.badRequest()
                    .body(new PaymentResponse(false, null, "Некорректный CVV"));
        }
        return ResponseEntity.ok(
                new PaymentResponse(true, UUID.randomUUID().toString(), "Оплата успешно выполнена")
        );

    }


}