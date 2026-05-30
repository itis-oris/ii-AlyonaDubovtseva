package ru.kpfu.itis.subscribio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kpfu.itis.subscribio.model.SubscriptionProduct;

import java.util.List;

public interface SubscriptionProductRepository extends JpaRepository<SubscriptionProduct, Long> {

    @Query("""
            select p from SubscriptionProduct p
            where p.id in (
                select oi.product.id from OrderItem oi
            )
            """)
    List<SubscriptionProduct> findProductsThatWereOrdered();
}