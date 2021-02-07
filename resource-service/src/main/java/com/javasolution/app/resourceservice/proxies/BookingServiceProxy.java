package com.javasolution.app.resourceservice.proxies;

import com.javasolution.app.resourceservice.responses.Booking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("booking-service")
public interface BookingServiceProxy {

    @DeleteMapping("/bookings/details")
    ResponseEntity<?> deleteBookingBySlotId(@RequestParam final long slotId);

    @DeleteMapping("/scheduling/{slotId}")
    ResponseEntity<?> deleteBookingBySlotIdScheduling(@PathVariable final long slotId);

    @GetMapping("/scheduling/{slotId}")
    Booking getBookingBySlotId(@PathVariable final long slotId);
}
