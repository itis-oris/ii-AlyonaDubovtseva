package ru.kpfu.itis.subscribio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.subscribio.dto.ProductDto;
import ru.kpfu.itis.subscribio.form.ProductForm;
import ru.kpfu.itis.subscribio.model.SubscriptionProduct;
import ru.kpfu.itis.subscribio.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "REST API для подписок")
public class ProductRestController {

    private final ProductService productService;

    @Operation(summary = "Получить все подписки")
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll() {
        List<ProductDto> products = productService.findAllForAdmin()
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(products);
    }


    @Operation(summary = "Получить подписку по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id){
        SubscriptionProduct product = productService.findById(id);
        return ResponseEntity.ok(toDto(product));
    }

    @Operation(summary = "Создать подписку")
    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductForm form) {
        SubscriptionProduct product = productService.create(form);
        return ResponseEntity.ok(toDto(product));
    }


    @Operation(summary = "Обновить подписку")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductForm form) {
        SubscriptionProduct product = productService.update(id, form);
        return ResponseEntity.ok(toDto(product));
    }


    @Operation(summary = "Удалить подписку")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ProductDto toDto(SubscriptionProduct product){
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .priceRub(product.getPriceRub())
                .imageUrl(product.getImageUrl())
                .durationLabel(product.getDurationLabel())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .active(product.isActive()).build();
    }

}


