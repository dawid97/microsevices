package com.javasolution.app.bookingservice.proxies;

import com.javasolution.app.bookingservice.responses.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserServiceProxy {

    @GetMapping("/users/details")
    User getUserByEmail(@RequestParam String email);

    @GetMapping("/users/{userId}")
    User getUser(@PathVariable final long userId);

    @GetMapping("/scheduling/{userId}")
    User getUserScheduling(@PathVariable final long userId);
}
