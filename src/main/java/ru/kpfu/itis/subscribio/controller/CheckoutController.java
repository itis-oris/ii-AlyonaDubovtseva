package ru.kpfu.itis.subscribio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.subscribio.dto.CartResponse;
import ru.kpfu.itis.subscribio.dto.CheckoutItemDto;
import ru.kpfu.itis.subscribio.form.PaymentForm;
import ru.kpfu.itis.subscribio.model.Order;
import ru.kpfu.itis.subscribio.security.UserDetailsImpl;
import ru.kpfu.itis.subscribio.service.CartService;
import ru.kpfu.itis.subscribio.service.CurrencyService;
import ru.kpfu.itis.subscribio.service.OrderService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final CurrencyService currencyService;

    @GetMapping("/checkout")
    public String checkoutPage(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(defaultValue = "RUB") String currency, Model model) {
        CartResponse cart = cartService.getCart(userDetails.getUser());
        List<CheckoutItemDto> checkoutItems = cart.getItems().stream()
                .map(item -> {
                    BigDecimal price =convert(item.getPriceRub(), currency);
                    BigDecimal subtotal = convert(item.getSubtotalRub(), currency);
                    return CheckoutItemDto.builder()
                            .title(item.getTitle())
                            .quantity(item.getQuantity())
                            .price(price)
                            .subtotal(subtotal).build();
                }).toList();
        BigDecimal totalInSelectedCurrency = convert(cart.getTotalRub(), currency);
        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setCurrency(currency);
        model.addAttribute("cart", cart);
        model.addAttribute("checkoutItems", checkoutItems);
        model.addAttribute("selectedCurrency", currency);
        model.addAttribute("selectedAmount", totalInSelectedCurrency);
        model.addAttribute("paymentForm", paymentForm);
        return "checkout/index";
    }

    @PostMapping("/checkout")
    public String processCheckout(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @ModelAttribute("paymentForm") PaymentForm paymentForm,
            BindingResult bindingResult, Model model) {
        CartResponse cart = cartService.getCart(userDetails.getUser());
        List<CheckoutItemDto> checkoutItems = cart.getItems().stream()
                .map(item -> CheckoutItemDto.builder()
                        .title(item.getTitle())
                        .quantity(item.getQuantity())
                        .price(convert(item.getPriceRub(), paymentForm.getCurrency()))
                        .subtotal(convert(item.getSubtotalRub(), paymentForm.getCurrency()))
                        .build())
                .toList();
        BigDecimal totalInSelectedCurrency = convert(cart.getTotalRub(), paymentForm.getCurrency());
        model.addAttribute("cart", cart);
        model.addAttribute("checkoutItems", checkoutItems);
        model.addAttribute("selectedCurrency", paymentForm.getCurrency());
        model.addAttribute("selectedAmount", totalInSelectedCurrency);
        model.addAttribute("paymentForm", paymentForm);
        if (bindingResult.hasErrors()) {
            return "checkout/index";}
        try {
            Order order = orderService.createPaidOrder(userDetails.getUser(), paymentForm);
            return "redirect:/checkout/success/" + order.getId();
        } catch (Exception e) {
            model.addAttribute("checkoutError", e.getMessage());
            return "checkout/index";
        }


    }

    private BigDecimal convert(BigDecimal amountRub, String currency) {
        if ("RUB".equalsIgnoreCase(currency))return amountRub.setScale(2,RoundingMode.HALF_UP);
        return currencyService.convertRubTo(currency, amountRub).setScale(2, RoundingMode.HALF_UP);

    }

    @GetMapping("/checkout/success/{orderId}")
    public String success(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId",orderId);
        return "checkout/success";
    }


}


