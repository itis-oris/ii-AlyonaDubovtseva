package ru.kpfu.itis.subscribio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kpfu.itis.subscribio.security.UserDetailsImpl;
import ru.kpfu.itis.subscribio.service.CartService;

@Controller
@RequiredArgsConstructor
public class CartPageController {

    private final CartService cartService;

    @GetMapping("/cart")
    public String cartPage(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        model.addAttribute("cart", cartService.getCart(userDetails.getUser()));
        return "cart/index";
    }

}

