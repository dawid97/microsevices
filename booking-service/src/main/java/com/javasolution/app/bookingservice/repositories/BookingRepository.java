package com.javasolution.app.bookingservice.repositories;

import com.javasolution.app.bookingservice.entities.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findAllByUserId(final long userId);
    Optional<Booking> findBySlotId(final long slotId);
}
