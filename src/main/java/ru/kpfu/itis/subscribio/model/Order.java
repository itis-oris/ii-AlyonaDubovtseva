package ru.kpfu.itis.subscribio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.kpfu.itis.subscribio.converter.OrderStatusJpaConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal totalRub;

    @Column(nullable = false)
    private String currency = "RUB";

    @Column(nullable = false)
    private BigDecimal totalInSelectedCurrency;

    @Convert(converter = OrderStatusJpaConverter.class)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @Column(columnDefinition = "TEXT")
    private String activationKeys;

    private LocalDateTime keySentAt;
}