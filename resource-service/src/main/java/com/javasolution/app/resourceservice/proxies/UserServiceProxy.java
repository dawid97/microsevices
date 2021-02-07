package com.javasolution.app.resourceservice.proxies;


import com.javasolution.app.resourceservice.responses.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("user-service")
public interface UserServiceProxy {

    @GetMapping("/users/details")
    User getUserByEmail(@RequestParam String email);
}