package org.example.dollarproduct.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dollarproduct.global.TimeStamped;
import org.example.dollarproduct.product.dto.request.ProductUpdateRequest;
import org.example.dollarproduct.product.dto.request.StockUpdateRequest;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private Long price;
    @Column
    private String description;
    @Column
    private Long stock;
    @Column
    private String photo;
    @Column
    private boolean state;
    @Column
    private Long userId;

    @Builder
    public Product(String name, Long price, String description, Long stock, String photo,
        Long userId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.photo = photo;
        this.userId = userId;
        this.state = true;
    }

    public void update(ProductUpdateRequest productRequest) {
        this.name = productRequest.getName();
        this.price = productRequest.getPrice();
        this.description = productRequest.getDescription();
        this.photo = productRequest.getPhoto();
    }

    public void updateStock(StockUpdateRequest stockupdateRequest) {
        this.stock = stockupdateRequest.getStock();
    }

    public void delete() {
        this.state = false;
    }

    public void updateStockAfterOrder(Long quantity) {
        this.stock = stock - quantity;
    }
}
