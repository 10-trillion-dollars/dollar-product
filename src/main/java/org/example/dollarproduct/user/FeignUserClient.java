package org.example.dollarproduct.user;

import org.example.share.config.global.entity.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dollar-user", url = "http://localhost:8082/external")
public interface FeignUserClient {

    @GetMapping("/users/{userId}")
    User findById(@PathVariable Long userId);
}
