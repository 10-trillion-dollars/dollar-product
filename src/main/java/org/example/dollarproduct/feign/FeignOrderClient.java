package org.example.dollarproduct.feign;

import feign.FeignException.FeignClientException;
import java.util.List;
import org.example.dollarproduct.entity.Order;
import org.example.dollarproduct.entity.OrderDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dollar-order", url = "https://order.10-trillon-dollars.com/external")
//@FeignClient(name = "dollar-order", url = "http://localhost:8084/external")
public interface FeignOrderClient {

    @GetMapping("/{productId}/orderDetails")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, maxDelay = 5000)
        , noRetryFor = {FeignClientException.class}
    )
    List<OrderDetail> findOrderDetailsByProductId(@PathVariable Long productId);

    @GetMapping("/orders/{orderId}")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, maxDelay = 5000)
        , noRetryFor = {FeignClientException.class}
    )
    Order getById(@PathVariable Long orderId);
}
