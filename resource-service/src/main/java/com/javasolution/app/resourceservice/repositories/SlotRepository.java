package com.javasolution.app.resourceservice.repositories;

import com.javasolution.app.resourceservice.entities.Slot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SlotRepository extends CrudRepository<Slot, Long> {
    List<Slot> findAllByResourceId(final long resourceId);
    List<Slot> findAllByIsBooked(final Boolean isBooked);
}
