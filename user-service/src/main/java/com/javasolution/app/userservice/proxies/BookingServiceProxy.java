package com.javasolution.app.userservice.proxies;

import com.javasolution.app.userservice.responses.Booking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient("booking-service")
public interface BookingServiceProxy {

    @DeleteMapping("/bookings/details")
    ResponseEntity<?> deleteBookingBySlotId(@RequestParam final long slotId);

    @GetMapping("/bookings/details")
    List<Booking> getBookingsByUserId(@RequestParam final long userId);
}
