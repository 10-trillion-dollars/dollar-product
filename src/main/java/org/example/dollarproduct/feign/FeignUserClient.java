package org.example.dollarproduct.feign;

import org.example.share.config.global.entity.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dollar-user", url = "https://user.10-trillon-dollars.com/external")
public interface FeignUserClient {

    @GetMapping("/users/{userId}")
    User findById(@PathVariable Long userId);
}
