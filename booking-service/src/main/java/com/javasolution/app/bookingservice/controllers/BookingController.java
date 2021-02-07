package com.javasolution.app.bookingservice.controllers;

import com.javasolution.app.bookingservice.entities.Booking;
import com.javasolution.app.bookingservice.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> addBooking(@RequestBody final Booking booking, final Principal principal){
        return new ResponseEntity<>(bookingService.addBooking(booking, principal), HttpStatus.CREATED);
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> getBookings(final Principal principal){
        return new ResponseEntity<>(bookingService.getBookings(principal), HttpStatus.OK);
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<?> getBooking(@PathVariable final long id, final Principal principal){
        return new ResponseEntity<>(bookingService.getBooking(id, principal), HttpStatus.OK);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable final long id, final Principal principal){
        bookingService.deleteBooking(id, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/bookings/details")
    public ResponseEntity<?> deleteBookingBySlotId(@RequestParam final long slotId){
        bookingService.deleteBySlotId(slotId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/scheduling/{slotId}")
    public Booking getBookingBySlotId(@PathVariable final long slotId){
        return bookingService.getBySlotIdScheduling(slotId);
    }

    @GetMapping("/bookings/details")
    public ResponseEntity<?> getBookingsByUserId(@RequestParam final long userId){
        return new ResponseEntity<>(bookingService.getBookingsByUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping("/scheduling/{slotId}")
    ResponseEntity<?> deleteBookingBySlotIdScheduling(@PathVariable final long slotId){
        bookingService.deleteBySlotIdScheduling(slotId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
