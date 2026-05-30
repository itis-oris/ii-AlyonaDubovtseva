package ru.kpfu.itis.subscribio.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.subscribio.dto.ExchangeRateApiResponse;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateClient {

    @Value("${exchange-rate-api.key}")
    private String apiKey;
    private final ObjectMapper objectMapper;

    public BigDecimal convertRubTo(String targetCurrency, BigDecimal amountRub) {
        log.info("Запрос конвертации {} RUB в {}", amountRub, targetCurrency);
        try {
            String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/RUB/" + targetCurrency + "/" + amountRub;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Ответ от ExchangeRate API: HTTP {}", response.statusCode());
            if (response.statusCode() != 200) {
                log.error("Ошибка ExchangeRate API: HTTP {}", response.statusCode());
                throw new IllegalStateException("ExchangeRate API вернул статус " + response.statusCode());
            }
            ExchangeRateApiResponse apiResponse = objectMapper.readValue(response.body(), ExchangeRateApiResponse.class);
            log.info("Конвертация успешна: {} RUB = {} {}", amountRub, apiResponse.getConversionResult(), targetCurrency);
            if (!"success".equals(apiResponse.getResult())) {
                throw new IllegalStateException("ExchangeRate API не смог выполнить конвертацию");
            }
            return apiResponse.getConversionResult();
        } catch (Exception e) {
            log.error("Ошибка при обращении к ExchangeRate API", e);
            throw new IllegalStateException("Ошибка при обращении к ExchangeRate API", e);
        }


    }

}


