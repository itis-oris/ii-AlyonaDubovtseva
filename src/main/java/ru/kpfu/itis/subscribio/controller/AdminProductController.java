package ru.kpfu.itis.subscribio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.subscribio.form.ProductForm;
import ru.kpfu.itis.subscribio.model.SubscriptionProduct;
import ru.kpfu.itis.subscribio.service.CategoryService;
import ru.kpfu.itis.subscribio.service.ProductService;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model){
        model.addAttribute("products", productService.findAllForAdmin());
        return "admin/products/list";
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/products/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("productForm") ProductForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("categories", categoryService.findAll());
            return "admin/products/form";
        }
        productService.create(form);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        SubscriptionProduct product = productService.findById(id);
        model.addAttribute("productId", id);
        model.addAttribute("productForm", productService.toForm(product));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/products/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("productForm") ProductForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            model.addAttribute("categories", categoryService.findAll());
            return "admin/products/form";
        }
        productService.update(id, form);
        return "redirect:/admin/products";


    }


    @GetMapping("/ordered")
    public String orderedProducts(Model model) {
        model.addAttribute("products", productService.findOrderedProducts());
        return "admin/products/ordered";
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }


}


