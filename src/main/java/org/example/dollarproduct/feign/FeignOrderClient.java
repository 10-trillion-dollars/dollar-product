package org.example.dollarproduct.feign;

import java.util.List;
import org.example.dollarproduct.entity.Order;
import org.example.dollarproduct.entity.OrderDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dollar-order", url = "http://localhost:8084/external")

public interface FeignOrderClient {

    @GetMapping("/orders/{orderId}")
    Order getById(@PathVariable Long orderId);

    // Product ID에 따른 OrderDetail 정보를 반환하는 새로운 메서드 추가
    @GetMapping("/admin/products/{productId}/orderDetails")
    List<OrderDetail> findOrderDetailsByProductId(@PathVariable Long productId);
}
