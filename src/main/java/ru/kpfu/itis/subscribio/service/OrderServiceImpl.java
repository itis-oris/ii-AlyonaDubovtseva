package ru.kpfu.itis.subscribio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.subscribio.client.PaymentClient;
import ru.kpfu.itis.subscribio.form.PaymentForm;
import ru.kpfu.itis.subscribio.model.*;
import ru.kpfu.itis.subscribio.repository.CartItemRepository;
import ru.kpfu.itis.subscribio.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentClient paymentClient;
    private final CurrencyService currencyService;
    private final EmailService emailService;

    @Override
    public List<Order> findUserOrders(User user) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @Override
    public List<Order> findAllOrdersForAdmin() {
        return orderRepository.findAllWithUsersOrderByCreatedAtDesc();

    }

    @Override
    @Transactional
    public Order createPaidOrder(User user, PaymentForm paymentForm) {
        log.info("Создание оплаченного заказа для пользователя id={}, email={}",
                user.getId(), user.getEmail());
        List<CartItem> cartItems = cartItemRepository.findByUserIdOrderByIdAsc(user.getId());
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Корзина пустая");
        }
        BigDecimal totalRub = cartItems.stream()
                .map(item -> item.getProduct().getPriceRub()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        String currency = paymentForm.getCurrency();
        BigDecimal totalInSelectedCurrency;
        if ("RUB".equalsIgnoreCase(currency)) {
            totalInSelectedCurrency = totalRub;
        } else {
            totalInSelectedCurrency = currencyService.convertRubTo(currency, totalRub);
        }
        log.info("Сумма заказа: {} RUB, валюта оплаты: {}", totalRub, paymentForm.getCurrency());
        totalInSelectedCurrency = totalInSelectedCurrency.setScale(2, java.math.RoundingMode.HALF_UP);
        boolean paid = paymentClient.pay(totalInSelectedCurrency, paymentForm);
        if (!paid) {
            throw new IllegalStateException("Оплата не прошла");
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PAID);
        order.setTotalRub(totalRub);
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setProductTitle(cartItem.getProduct().getTitle());
            orderItem.setPriceRub(cartItem.getProduct().getPriceRub());
            orderItem.setQuantity(cartItem.getQuantity());
            order.getItems().add(orderItem);
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountRub(totalRub);
        payment.setCurrency(paymentForm.getCurrency());
        payment.setAmountInSelectedCurrency(totalInSelectedCurrency);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        payment.setCardLastFour(paymentForm.getCardNumber().substring(12));
        order.setPayment(payment);
        order.setCurrency(currency);
        order.setTotalInSelectedCurrency(totalInSelectedCurrency);
        Order savedOrder = orderRepository.save(order);
        log.info("Заказ {} успешно создан для пользователя {}",
                savedOrder.getOrderNumber(), user.getEmail());
        cartItemRepository.deleteByUserId(user.getId());
        return savedOrder;


    }

    private String generateOrderNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long number = orderRepository.count() + 1;
        return String.format("ORD-%s-%04d", datePart, number);
    }
    @Override
    @Transactional
    public void sendKeys(Long orderId, String activationKeys) {
        log.info("Администратор отправляет ключи по заказу id={}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Нельзя отправить ключи для отменённого заказа");
        }
        emailService.sendActivationKeys(order, activationKeys);
        order.setActivationKeys(activationKeys);
        order.setKeySentAt(LocalDateTime.now());
        order.setStatus(OrderStatus.KEY_SENT);
        orderRepository.save(order);
        log.info("Заказ {} переведён в статус KEY_SENT", order.getOrderNumber());
    }

    @Override
    public Order findOrderForAdmin(Long id) {
        return orderRepository.findByIdWithUserAndItems(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        log.info("Попытка отмены заказа id={}", id);
        Order order = findOrderForAdmin(id);
        if (order.getStatus() == OrderStatus.KEY_SENT) {
            throw new IllegalStateException("Нельзя отменить заказ, по которому уже отправлены ключи");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        log.info("Заказ {} отменён", order.getOrderNumber());
    }



}



