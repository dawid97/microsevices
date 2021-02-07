package com.javasolution.app.bookingservice.services;

import com.javasolution.app.bookingservice.entities.Booking;
import com.javasolution.app.bookingservice.exceptions.AlreadyExistException;
import com.javasolution.app.bookingservice.exceptions.NotFoundException;
import com.javasolution.app.bookingservice.proxies.ResourceServiceProxy;
import com.javasolution.app.bookingservice.proxies.UserServiceProxy;
import com.javasolution.app.bookingservice.repositories.BookingRepository;
import com.javasolution.app.bookingservice.responses.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ResourceServiceProxy resourceServiceProxy;
    private final UserServiceProxy userServiceProxy;

    public Booking addBooking(final Booking booking, final Principal principal) {

        //check if slot exists
        final Slot slot = resourceServiceProxy.getSlot(booking.getSlotId());

        //check if booked
        if(slot.getIsBooked())
            throw new AlreadyExistException("slot", "Slot is already booked");

        //assign user
        if(booking.getUserId() == null) {
            final User user = userServiceProxy.getUserByEmail(principal.getName());
            booking.setUserId(user.getId());
        }else {
            //check if exists
            userServiceProxy.getUser(booking.getUserId());
        }

        //update slot to booked
        resourceServiceProxy.updateSlot(true, slot.getId());

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookings(final Principal principal) {

        //find user
        final User user = userServiceProxy.getUserByEmail(principal.getName());

        final List<Booking> bookings;

        //decide which bookings to return
        if(user.getUserRole().equals(UserRole.ROLE_CUSTOMER))
            bookings = bookingRepository.findAllByUserId(user.getId());
        else
            bookings = (List<Booking>) bookingRepository.findAll();

        //complete additional information
        for(Booking booking : bookings){
            booking.setSlot(resourceServiceProxy.getSlot(booking.getSlotId()));
            booking.setResource(resourceServiceProxy.getResource(booking.getSlot().getResourceId()));
            booking.setUser(userServiceProxy.getUser(booking.getUserId()));
        }

        //limit booking when resource role
        if(user.getUserRole().equals(UserRole.ROLE_RESOURCE)){
            bookings.removeIf(booking -> !booking.getResource().getUserId().equals(user.getId()));
        }

        return bookings;
    }

    public void deleteBooking(final long id, final Principal principal) {

        //check if booking exists
        final Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("booking", "booking with id " + id + " was not found"));

        //find user
        final User user = userServiceProxy.getUserByEmail(principal.getName());

        //check if owner when customer role
        if(user.getUserRole().equals(UserRole.ROLE_CUSTOMER) && !booking.getUserId().equals(user.getId())){
            throw new NotFoundException("booking", "not owner");
        }

        //check if owner when resource role
        if(user.getUserRole().equals(UserRole.ROLE_RESOURCE)){
            final Slot slot = resourceServiceProxy.getSlot(booking.getSlotId());
            final Resource resource = resourceServiceProxy.getResource(slot.getResourceId());
            if(!resource.getUserId().equals(user.getId())){
                throw new NotFoundException("booking", "not owner");
            }
        }

        resourceServiceProxy.updateSlot(false, booking.getSlotId());
        bookingRepository.deleteById(id);
    }

    public void deleteBySlotId(final long slotId) {

        //check if exists
        final Booking booking = bookingRepository.findBySlotId(slotId)
                .orElseThrow(()-> new NotFoundException("booking", "booking with slot id " + slotId + " was not found" ));

        bookingRepository.deleteById(booking.getId());
    }

    public List<Booking> getBookingsByUserId(final long userId) {
        return bookingRepository.findAllByUserId(userId);
    }

    public Booking getBooking(final long id, final Principal principal) {

        final Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("booking","booking with id " + id + " was not found"));

        //complete additional information
         booking.setSlot(resourceServiceProxy.getSlot(booking.getSlotId()));
         booking.setResource(resourceServiceProxy.getResource(booking.getSlot().getResourceId()));
         booking.setUser(userServiceProxy.getUser(booking.getUserId()));

         return booking;
    }

    public Booking getBySlotIdScheduling(final long slotId) {

        final Booking booking = bookingRepository.findBySlotId(slotId)
                .orElseThrow(()-> new NotFoundException("booking", "booking with slot id " + slotId + " was not found"));

        booking.setUser(userServiceProxy.getUserScheduling(booking.getUserId()));
        return booking;
    }

    public void deleteBySlotIdScheduling(final long slotId) {

        final Booking booking = bookingRepository.findBySlotId(slotId)
                .orElseThrow(()-> new NotFoundException("booking", "booking with slot id " + slotId + " was not found"));

        bookingRepository.deleteById(booking.getId());
    }
}
