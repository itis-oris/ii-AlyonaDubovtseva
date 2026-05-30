package ru.kpfu.itis.subscribio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.subscribio.dto.CartItemDto;
import ru.kpfu.itis.subscribio.dto.CartResponse;
import ru.kpfu.itis.subscribio.model.CartItem;
import ru.kpfu.itis.subscribio.model.SubscriptionProduct;
import ru.kpfu.itis.subscribio.model.User;
import ru.kpfu.itis.subscribio.repository.CartItemRepository;
import ru.kpfu.itis.subscribio.repository.SubscriptionProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final SubscriptionProductRepository productRepository;

    @Override
    public CartResponse getCart(User user) {
        return buildResponse(cartItemRepository.findByUserIdOrderByIdAsc(user.getId()));
    }

    @Override
    @Transactional
    public CartResponse addProduct(User user, Long productId) {
        SubscriptionProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
        if (!product.isActive()) {
            throw new IllegalArgumentException("Товар недоступен");
        }
        CartItem item = cartItemRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setUser(user);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    return newItem;
                });
        item.setQuantity(item.getQuantity()+ 1);
        cartItemRepository.save(item);
        return getCart(user);
    }



    @Override
    @Transactional
    public CartResponse increase(User user, Long cartItemId) {
        CartItem item = findUserCartItem(user, cartItemId);
        item.setQuantity(item.getQuantity() + 1);
        cartItemRepository.save(item);
        return getCart(user);
    }

    @Override
    @Transactional
    public CartResponse decrease(User user, Long cartItemId) {
        CartItem item = findUserCartItem(user, cartItemId);
        if (item.getQuantity() <= 1 ) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(item.getQuantity() - 1);
            cartItemRepository.save(item);
        }
        return getCart(user);
    }

    @Override
    @Transactional
    public CartResponse remove(User user, Long cartItemId) {
        CartItem item = findUserCartItem(user, cartItemId);
        cartItemRepository.delete(item);
        return getCart(user);
    }

    @Override
    @Transactional
    public void clear(User user) {
        cartItemRepository.deleteByUserId(user.getId());
    }

    private CartItem findUserCartItem(User user, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Позиция корзины не найдена"));
        if (!item.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Нет доступа к этой позиции корзины");
        }
        return item;
    }

    private CartResponse buildResponse(List<CartItem> items) {
        List<CartItemDto> itemDtos = items.stream()
                .map(item -> {
                    BigDecimal subtotal = item.getProduct().getPriceRub()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                    return CartItemDto.builder()
                            .cartItemId(item.getId())
                            .productId(item.getProduct().getId())
                            .title(item.getProduct().getTitle())
                            .priceRub(item.getProduct().getPriceRub())
                            .quantity(item.getQuantity())
                            .subtotalRub(subtotal)
                            .build();
                })
                .toList();
        BigDecimal total = itemDtos.stream()
                .map(CartItemDto::getSubtotalRub)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalQuantity = itemDtos.stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();
        return CartResponse.builder()
                .items(itemDtos)
                .totalRub(total)
                .totalQuantity(totalQuantity)
                .build();
    }

}



