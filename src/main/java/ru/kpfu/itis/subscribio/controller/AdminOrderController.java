package ru.kpfu.itis.subscribio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.subscribio.service.OrderService;

@Controller
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/admin/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.findAllOrdersForAdmin());
        return "admin/orders/list";
    }

    @GetMapping("/admin/orders/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findOrderForAdmin(id));
        return "admin/orders/details";
    }


    @PostMapping("/admin/orders/{id}/send-keys")
    public String sendKeys(@PathVariable Long id, @RequestParam String activationKeys, RedirectAttributes redirectAttributes) {
        try {
            orderService.sendKeys(id, activationKeys);
            redirectAttributes.addFlashAttribute("success", "Ключи отправлены пользователю на почту");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Не удалось отправить письмо: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }


    @PostMapping("/admin/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("success", "Заказ отменён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Не удалось отменить заказ: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }



}


