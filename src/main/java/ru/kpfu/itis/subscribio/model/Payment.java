package ru.kpfu.itis.subscribio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    private BigDecimal amountRub;
    private String currency;
    private BigDecimal amountInSelectedCurrency;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDateTime paidAt;
    private String cardLastFour;
}