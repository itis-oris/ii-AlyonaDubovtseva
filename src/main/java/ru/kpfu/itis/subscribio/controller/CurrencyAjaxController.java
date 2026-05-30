package ru.kpfu.itis.subscribio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.subscribio.dto.AjaxErrorResponse;
import ru.kpfu.itis.subscribio.dto.CartResponse;
import ru.kpfu.itis.subscribio.dto.CurrencyResponse;
import ru.kpfu.itis.subscribio.security.UserDetailsImpl;
import ru.kpfu.itis.subscribio.service.CartService;
import ru.kpfu.itis.subscribio.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
@Tag(name = "Currency API", description = "API для пересчёта суммы корзины в другую валюту")
public class CurrencyAjaxController {

    private final CartService cartService;
    private final CurrencyService currencyService;

    @Operation(summary = "Пересчитать сумму корзины в выбранную валюту")
    @GetMapping("/convert")
    public ResponseEntity<?> convert(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String currency) {
        try {
            CartResponse cart = cartService.getCart(userDetails.getUser());
            BigDecimal converted;
            if ("RUB".equalsIgnoreCase(currency)) {
                converted = cart.getTotalRub();
            } else {
                converted = currencyService.convertRubTo(currency, cart.getTotalRub());
            }
            converted = converted.setScale(2, RoundingMode.HALF_UP);
            return ResponseEntity.ok(
                    CurrencyResponse.builder()
                            .currency(currency)
                            .totalRub(cart.getTotalRub())
                            .convertedTotal(converted)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AjaxErrorResponse("Не удалось пересчитать валюту"));
        }

    }




}



