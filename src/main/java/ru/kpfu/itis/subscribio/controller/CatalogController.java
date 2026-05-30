package ru.kpfu.itis.subscribio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.subscribio.model.SubscriptionProduct;
import ru.kpfu.itis.subscribio.service.CategoryService;
import ru.kpfu.itis.subscribio.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping({"/", "/catalog"})
    public String catalog(Model model, @RequestParam(required = false) Long categoryId, @RequestParam(required = false) BigDecimal minPrice,
                          @RequestParam(required = false) BigDecimal maxPrice, @RequestParam(required = false) String query) {
        List<SubscriptionProduct> products =
                productService.findActiveProducts(categoryId, minPrice, maxPrice, query);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("query", query);
        return "catalog/index";
    }

    @GetMapping("/products/{id}")
    public String productPage(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "catalog/product";
    }

}




