package ru.kpfu.itis.subscribio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kpfu.itis.subscribio.security.UserDetailsImpl;
import ru.kpfu.itis.subscribio.service.OrderService;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final OrderService orderService;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("orders", orderService.findUserOrders(userDetails.getUser()));
        return "profile/index";
    }
}

