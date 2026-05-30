package ru.kpfu.itis.subscribio.service;

import ru.kpfu.itis.subscribio.dto.CartResponse;
import ru.kpfu.itis.subscribio.model.User;

public interface CartService {
    CartResponse getCart(User user);
    CartResponse addProduct(User user, Long productId);
    CartResponse increase(User user, Long cartItemId);
    CartResponse decrease(User user, Long cartItemId);
    CartResponse remove(User user, Long cartItemId);
    void clear(User user);
}