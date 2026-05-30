package ru.kpfu.itis.subscribio.service;

import ru.kpfu.itis.subscribio.form.ProductForm;
import ru.kpfu.itis.subscribio.model.SubscriptionProduct;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<SubscriptionProduct> findCatalog(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String query);
    List<SubscriptionProduct> findAllForAdmin();
    SubscriptionProduct findById(Long id);
    SubscriptionProduct create(ProductForm form);
    SubscriptionProduct update(Long id, ProductForm form);
    void delete(Long id);
    ProductForm toForm(SubscriptionProduct product);
    List<SubscriptionProduct> findActiveProducts(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String query);
    List<SubscriptionProduct> findOrderedProducts();
}

