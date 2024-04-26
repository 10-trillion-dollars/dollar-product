package org.example.dollarproduct.feign;

import java.util.List;
import org.example.dollarproduct.entity.Order;
import org.example.dollarproduct.entity.OrderDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "dollar-order", url = "${loadbalancer.order}/external")
//@FeignClient(name = "dollar-order", url = "http://localhost:8084/external")

public interface FeignOrderClient {


    @GetMapping("/{productId}/orderDetails")
    List<OrderDetail> findOrderDetailsByProductId(@PathVariable Long productId);


//    @GetMapping("/orders/{orderId}")
//    Order getById(@PathVariable Long orderId);

    @GetMapping("/orders")
    List<Order> getAllById(@RequestBody List<Long> orderIdList);


}


