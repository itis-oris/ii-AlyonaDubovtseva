package ru.kpfu.itis.subscribio.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.subscribio.model.Category;
import ru.kpfu.itis.subscribio.model.SubscriptionProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SubscriptionProductDao {

    @PersistenceContext
    private EntityManager entityManager;
    public List<SubscriptionProduct> findByFilters(Long categoryId, BigDecimal minPrice,
            BigDecimal maxPrice, String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SubscriptionProduct> cq = cb.createQuery(SubscriptionProduct.class);
        Root<SubscriptionProduct> product = cq.from(SubscriptionProduct.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isTrue(product.get("active")));
        if (categoryId != null) {
            Join<SubscriptionProduct, Category> category = product.join("category");
            predicates.add(cb.equal(category.get("id"), categoryId));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(product.get("priceRub"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(product.get("priceRub"), maxPrice));
        }
        if (query != null && !query.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(product.get("title")),
                            "%" + query.toLowerCase() + "%"
                    )


            );
        }
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(product.get("priceRub")));
        return entityManager.createQuery(cq).getResultList();
    }

}


