package org.example.dollarproduct.product.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.dollarproduct.product.entity.Product;

@Getter
public class FeignProductResponse {
    Product product;

    public FeignProductResponse(Product product) {
        this.product = product;
    }

}
