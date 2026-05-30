package ru.kpfu.itis.subscribio.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.subscribio.dto.payment.PaymentServiceRequest;
import ru.kpfu.itis.subscribio.dto.payment.PaymentServiceResponse;
import ru.kpfu.itis.subscribio.form.PaymentForm;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentClient {

    @Value("${payment-service.url}")
    private String paymentServiceUrl;
    private final ObjectMapper objectMapper;
    public boolean pay(BigDecimal amountRub, PaymentForm form) {
        try {
            log.info("Отправка запроса в payment-service. Сумма: {}, валюта: {}",amountRub, form.getCurrency());
            PaymentServiceRequest requestBody = PaymentServiceRequest.builder().amount(amountRub)
                    .currency(form.getCurrency())
                    .cardNumber(form.getCardNumber())
                    .expiryDate(form.getExpiryDate())
                    .cvv(form.getCvv())
                    .build();
            String json = objectMapper.writeValueAsString(requestBody);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(paymentServiceUrl+ "/api/payments")).header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Ответ от payment-service. HTTP статус: {}", response.statusCode());
            if (response.statusCode() != 200) {
                log.warn("Payment-service вернул ошибку. Статус: {}, тело ответа: {}", response.statusCode(), response.body());
                return false;
            }

            PaymentServiceResponse paymentResponse = objectMapper.readValue(response.body(), PaymentServiceResponse.class);
            log.info("Платёж обработан. transactionId: {}, success: {}",
                    paymentResponse.getTransactionId(), paymentResponse.isSuccess());
            return paymentResponse.isSuccess();

        } catch (Exception e) {
            log.error("Ошибка при обращении к payment-service", e);
            throw new IllegalStateException("Ошибка при обращении к payment-service", e);
        }


    }

}




