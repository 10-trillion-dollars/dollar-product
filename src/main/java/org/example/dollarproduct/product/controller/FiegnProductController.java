package org.example.dollarproduct.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.dollarproduct.product.dto.response.FeignProductResponse;
import org.example.dollarproduct.product.entity.Product;
import org.example.dollarproduct.product.service.ProductService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external")
public class FiegnProductController {
    private final ProductService productService;

    @GetMapping("/products/{productId}")
    public Product getProducts(@PathVariable Long productId) throws NotFoundException {
        return productService.getProduct(productId);
    }
}
