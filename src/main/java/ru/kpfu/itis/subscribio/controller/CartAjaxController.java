package ru.kpfu.itis.subscribio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.subscribio.dto.AjaxErrorResponse;
import ru.kpfu.itis.subscribio.dto.CartResponse;
import ru.kpfu.itis.subscribio.security.UserDetailsImpl;
import ru.kpfu.itis.subscribio.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart API", description = "AJAX API для работы с корзиной")
public class CartAjaxController {
    private final CartService cartService;

    @Operation(summary = "Получить корзину текущего пользователя")
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails.getUser()));
    }

    @Operation(summary = "Добавить товар в корзину")
    @PostMapping("/add/{productId}")
    public ResponseEntity<?> add(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long productId) {
        try {
            return ResponseEntity.ok(cartService.addProduct(userDetails.getUser(),  productId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new AjaxErrorResponse(e.getMessage()));
        }


    }

    @Operation(summary = "Увеличить количество товара в корзине")
    @PostMapping("/increase/{cartItemId}")
    public ResponseEntity<?> increase(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long cartItemId) {
        try {
            return ResponseEntity.ok(cartService.increase(userDetails.getUser(), cartItemId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new AjaxErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Уменьшить количество товара в корзине")
    @PostMapping("/decrease/{cartItemId}")
    public ResponseEntity<?> decrease(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long cartItemId) {
        try {
            return ResponseEntity.ok(cartService.decrease(userDetails.getUser(), cartItemId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new AjaxErrorResponse(e.getMessage()));
        }


    }

    @Operation(summary = "Удалить товар из корзины")
    @PostMapping("/remove/{cartItemId}")
    public ResponseEntity<?> remove(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long cartItemId) {
        try {
            return ResponseEntity.ok(cartService.remove(userDetails.getUser(), cartItemId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new AjaxErrorResponse(e.getMessage()));
        }
    }


}


