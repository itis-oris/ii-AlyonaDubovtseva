package ru.kpfu.itis.subscribio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.subscribio.model.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserId(Long userId);
    List<CartItem> findByUserIdOrderByIdAsc(Long userId);
}