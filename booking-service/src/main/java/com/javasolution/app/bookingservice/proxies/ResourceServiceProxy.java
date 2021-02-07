package com.javasolution.app.bookingservice.proxies;

import com.javasolution.app.bookingservice.responses.Resource;
import com.javasolution.app.bookingservice.responses.Slot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "resource-service")
public interface ResourceServiceProxy {

    @GetMapping("/slots/{id}")
    Slot getSlot(@PathVariable final long id);

    @GetMapping("resources/{id}")
    Resource getResource(@PathVariable final long id);

    @PostMapping("/slots/{id}")
    Slot updateSlot(@RequestParam final boolean isBooked, @PathVariable final long id);
}
