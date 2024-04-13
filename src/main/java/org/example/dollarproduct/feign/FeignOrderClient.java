package org.example.dollarproduct.feign;

import java.util.List;
import org.example.dollarproduct.entity.Order;
import org.example.dollarproduct.entity.OrderDetail;
import org.example.share.config.global.entity.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dollar-order", url = "http://localhost:8084/external")

public interface FeignOrderClient {
    @GetMapping("/{productId}/orderDetails")
    List<OrderDetail> findOrderDetailsByProductId(@PathVariable Long productId);

    @GetMapping("/orders/{orderId}")
    Order getById(@PathVariable Long orderId);
}
