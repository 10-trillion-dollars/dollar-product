package org.example.dollarproduct.feign;

import feign.FeignException.FeignClientException;
import org.example.share.config.global.entity.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "dollar-user", url = "https://user.10-trillon-dollars.com/external")
@FeignClient(name = "dollar-user", url = "http://localhost:8082/external")
public interface FeignUserClient {

    @GetMapping("/users/{userId}")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, maxDelay = 5000)
        , noRetryFor = {FeignClientException.class}
    )
    User findById(@PathVariable Long userId);
}
