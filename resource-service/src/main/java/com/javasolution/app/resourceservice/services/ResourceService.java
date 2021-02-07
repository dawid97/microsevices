package com.javasolution.app.resourceservice.services;

import com.javasolution.app.resourceservice.entities.Resource;
import com.javasolution.app.resourceservice.entities.Slot;
import com.javasolution.app.resourceservice.exceptions.ExistsException;
import com.javasolution.app.resourceservice.proxies.BookingServiceProxy;
import com.javasolution.app.resourceservice.proxies.UserServiceProxy;
import com.javasolution.app.resourceservice.repositories.ResourceRepository;
import com.javasolution.app.resourceservice.repositories.SlotRepository;
import com.javasolution.app.resourceservice.responses.User;
import com.javasolution.app.resourceservice.responses.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final UserServiceProxy userServiceProxy;
    private final SlotRepository slotRepository;
    private final BookingServiceProxy bookingServiceProxy;

    public Resource addResource(final Resource resource, final Principal principal) {

        //check if duplicated
        if(resourceRepository.findByNameAndType(resource.getName(), resource.getType()) != null){
            throw new ExistsException("resource", "resource already exists");
        }

        //find user
        final User user = userServiceProxy.getUserByEmail(principal.getName());
        resource.setUserId(user.getId());

        return resourceRepository.save(resource);
    }

    public List<Resource> getResources(final Principal principal) {

        //find user
        final User user = userServiceProxy.getUserByEmail(principal.getName());

        if(user.getUserRole().equals(UserRole.ROLE_RESOURCE)){
            return resourceRepository.findAllByUserId(user.getId());
        }

        return (List<Resource>) resourceRepository.findAll();
    }

    public void deleteResource(final long id, final Principal principal) {

        //check if exists
        final Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ExistsException("resource","resource with id " + id + " was not found"));

        //find user
        final User user = userServiceProxy.getUserByEmail(principal.getName());

        //check if owner when resource role
        if(user.getUserRole().equals(UserRole.ROLE_RESOURCE)){
            resourceRepository.findByUserIdAndId(user.getId(), id)
                    .orElseThrow(() -> new ExistsException("resource","not owner"));
        }

        //find slots by resource id
        final List<Slot> slots = slotRepository.findAllByResourceId(resource.getId());

        //resource has no slots
        if(slots.isEmpty()){
            resourceRepository.deleteById(resource.getId());
            return;
        }

        //delete slots
        for(final Slot slot : slots){
            //delete booking if exists
            if(slot.getIsBooked()){
                bookingServiceProxy.deleteBookingBySlotId(slot.getId());
            }
            slotRepository.deleteById(slot.getId());
        }

        resourceRepository.deleteById(resource.getId());
    }

    public Resource getResource(final long id) {
        return resourceRepository.findById(id)
                .orElseThrow( ()-> new ExistsException("resource","resource with id " + id + " was not found"));
    }
}
