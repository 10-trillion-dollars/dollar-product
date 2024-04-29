package org.example.dollarproduct.feign;

import java.util.List;
import java.util.Map;
import org.example.dollarproduct.entity.Order;
import org.example.dollarproduct.entity.OrderDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dollar-order", url = "${loadbalancer.order}/external")
//@FeignClient(name = "dollar-order", url = "http://localhost:8084/external")
public interface FeignOrderClient {


    // 쿼리 개선 후
    @PostMapping("/productLists/orderDetails")
    List<OrderDetail> findOrderDetailsByProductId(@RequestBody List<Long> productList);

    @PostMapping("/orders")
    Map<Long, Order> getAllById(@RequestBody List<Long> orderIdList);


}


