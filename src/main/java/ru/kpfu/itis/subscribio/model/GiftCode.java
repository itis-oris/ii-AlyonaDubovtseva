package ru.kpfu.itis.subscribio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "gift_codes")
public class GiftCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private SubscriptionProduct product;
    @ManyToOne
    @JoinColumn(name = "order_item_id")

    private OrderItem orderItem;
    private boolean used = false;
    private LocalDateTime usedAt;
}