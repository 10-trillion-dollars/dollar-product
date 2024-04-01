package org.example.dollarproduct.product.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;

import org.example.dollarproduct.product.dto.response.ProductDetailResponse;
import org.example.dollarproduct.product.dto.response.ProductResponse;
import org.example.dollarproduct.product.service.ProductService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAllProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(
        @PathVariable Long productId
    ) throws NotFoundException {
        return ResponseEntity.status(200)
            .body(productService.getProductDetail(productId));
    }

    @GetMapping("/search")
    public List<ProductResponse> getSearchProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "") String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProductsBySearch(search, pageable);
    }


}
