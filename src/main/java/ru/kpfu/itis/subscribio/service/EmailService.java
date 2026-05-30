package ru.kpfu.itis.subscribio.service;

import ru.kpfu.itis.subscribio.model.Order;

public interface EmailService {
    void sendActivationKeys(Order order, String activationKeys);
}