package org.example.dollarproduct.feign;

import feign.FeignException;
import feign.FeignException.FeignClientException;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.dollarproduct.entity.Order;
import org.example.dollarproduct.entity.OrderDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "dollar-order", url = "https://order.10-trillon-dollars.com/external")
@FeignClient(name = "dollar-order", url = "http://localhost:8084/external")
public interface FeignOrderClient {


    @GetMapping("/{productId}/orderDetails")
    List<OrderDetail> findOrderDetailsByProductId(@PathVariable Long productId);


//    @GetMapping("/orders/{orderId}")
//    Order getById(@PathVariable Long orderId);

    @GetMapping("/orders")
    List<Order> getAllById(@RequestBody List<Long> orderIdList);


}


