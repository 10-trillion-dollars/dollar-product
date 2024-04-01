package org.example.dollarproduct.product.dto.response;

import lombok.Getter;
import org.example.dollarproduct.product.entity.Product;

@Getter
public class ProductDetailResponse {

    private Long id;
    private String name;
    private Long price;
    private String description;
    private Long stock;
    private String photo;
    private String adminname;

    public ProductDetailResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.stock = product.getStock();
        this.photo = product.getPhoto();
        this.adminname = product.getUser().getUsername();
    }
}
