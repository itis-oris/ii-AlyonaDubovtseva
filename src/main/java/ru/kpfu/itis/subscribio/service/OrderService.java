package ru.kpfu.itis.subscribio.service;

import ru.kpfu.itis.subscribio.form.PaymentForm;
import ru.kpfu.itis.subscribio.model.Order;
import ru.kpfu.itis.subscribio.model.User;

import java.util.List;

public interface OrderService {
    List<Order> findUserOrders(User user);
    List<Order> findAllOrdersForAdmin();
    Order createPaidOrder(User user, PaymentForm paymentForm);
    void sendKeys(Long orderId, String activationKeys);
    Order findOrderForAdmin(Long id);
    void cancelOrder(Long id);
}