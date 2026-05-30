package ru.kpfu.itis.subscribio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.subscribio.dao.SubscriptionProductDao;
import ru.kpfu.itis.subscribio.form.ProductForm;
import ru.kpfu.itis.subscribio.model.Category;
import ru.kpfu.itis.subscribio.model.SubscriptionProduct;
import ru.kpfu.itis.subscribio.repository.CategoryRepository;
import ru.kpfu.itis.subscribio.repository.SubscriptionProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final SubscriptionProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubscriptionProductDao productDao;

    @Override
    public List<SubscriptionProduct> findCatalog(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String query) {
        return productDao.findByFilters(categoryId, minPrice, maxPrice, query);
    }


    @Override
    public List<SubscriptionProduct> findAllForAdmin() {
        return productRepository.findAll();
    }

    @Override
    public SubscriptionProduct findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
    }


    @Override
    @Transactional
    public SubscriptionProduct create(ProductForm form) {
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
        SubscriptionProduct product = new SubscriptionProduct();
        fillProduct(product, form, category);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public SubscriptionProduct update(Long id, ProductForm form) {
        SubscriptionProduct product = findById(id);
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
        fillProduct(product, form, category);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        SubscriptionProduct product = findById(id);
        product.setActive(false);
        productRepository.save(product);
    }



    @Override
    public ProductForm toForm(SubscriptionProduct product) {
        ProductForm form = new ProductForm();
        form.setTitle(product.getTitle());
        form.setDescription(product.getDescription());
        form.setPriceRub(product.getPriceRub());
        form.setImageUrl(product.getImageUrl());
        form.setDurationLabel(product.getDurationLabel());
        form.setActive(product.isActive());
        if (product.getCategory() !=null) {
            form.setCategoryId(product.getCategory().getId());
        }
        return form;
    }

    @Override
    public List<SubscriptionProduct> findActiveProducts(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String query) {
        List<SubscriptionProduct> allFiltered = findCatalog(categoryId, minPrice, maxPrice, query);
        return allFiltered.stream()
                .filter(SubscriptionProduct::isActive)
                .toList();
    }

    private void fillProduct(SubscriptionProduct product, ProductForm form, Category category) {
        product.setTitle(form.getTitle());
        product.setDescription(form.getDescription());
        product.setPriceRub(form.getPriceRub());
        product.setImageUrl(form.getImageUrl());
        product.setDurationLabel(form.getDurationLabel());
        product.setActive(form.isActive());
        product.setCategory(category);
    }
    @Override
    public List<SubscriptionProduct> findOrderedProducts() {
        return productRepository.findProductsThatWereOrdered();
    }
}