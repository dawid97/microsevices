package com.javasolution.app.userservice.proxies;

import com.javasolution.app.userservice.responses.Slot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "resource-service")
public interface ResourceServiceProxy {

    @PostMapping("/slots/{id}")
    Slot updateSlot(@RequestParam final boolean isBooked, @PathVariable final long id);
}
