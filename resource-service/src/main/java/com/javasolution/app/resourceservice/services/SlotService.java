package com.javasolution.app.resourceservice.services;

import com.javasolution.app.resourceservice.entities.Resource;
import com.javasolution.app.resourceservice.entities.Slot;
import com.javasolution.app.resourceservice.exceptions.DivisionException;
import com.javasolution.app.resourceservice.exceptions.ExistsException;
import com.javasolution.app.resourceservice.exceptions.SlotsAlreadyExistException;
import com.javasolution.app.resourceservice.proxies.BookingServiceProxy;
import com.javasolution.app.resourceservice.proxies.UserServiceProxy;
import com.javasolution.app.resourceservice.repositories.ResourceRepository;
import com.javasolution.app.resourceservice.repositories.SlotRepository;
import com.javasolution.app.resourceservice.requests.LoginRequest;
import com.javasolution.app.resourceservice.responses.Booking;
import com.javasolution.app.resourceservice.responses.User;
import com.javasolution.app.resourceservice.responses.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class SlotService {

    private final ResourceRepository resourceRepository;
    private final SlotRepository slotRepository;
    private final UserServiceProxy userServiceProxy;
    private final BookingServiceProxy bookingServiceProxy;
    private final JavaMailSender javaMailSender;

    public List<Slot> addSlot(final Slot slot, final Principal principal) {

        final Duration timeBetweenStartAndEnd = Duration.between(
                slot.getStartTime(),
                slot.getFinishTime()
        );

        final long seconds = timeBetweenStartAndEnd.getSeconds();
        final long divisor = slot.getDivisor(); //in seconds

        if(seconds % divisor != 0)
            throw new DivisionException("resource","the time period cannot be divided into equal parts");

        final List<Slot> slots = new ArrayList<>();
        final long slotsNumber = seconds/divisor;

        //check if resource exists
        final Resource resource = resourceRepository.findById(slot.getResourceId())
                .orElseThrow(() -> new ExistsException("resource","resource with id " + slot.getResourceId() + " was not found"));

        //find user
        final User user = userServiceProxy.getUserByEmail(principal.getName());

        //check if owner when resource role
        if(user.getUserRole().equals(UserRole.ROLE_RESOURCE)
        && !resource.getUserId().equals(user.getId())
        ){
            throw new ExistsException("resource", "not owner");
        }

        //time period division
        for (int i = 0; i < slotsNumber; i++) {
            final Slot newSlot = new Slot();
            newSlot.setStartTime(slot.getStartTime().plusSeconds(divisor * i));
            newSlot.setFinishTime(newSlot.getStartTime().plusSeconds(divisor));
            newSlot.setResourceId(slot.getResourceId());
            slots.add(newSlot);
        }

        //check collisions
        final List<Slot> collisionSlots = checkCollision(slotRepository.findAllByResourceId(slot.getResourceId()), slots);

        //there are collisions
        if (!collisionSlots.isEmpty())
            throw new SlotsAlreadyExistException("Collision slots", collisionSlots);

        final List<Slot> savedSlots = new ArrayList<>();

        //there are no collisions
        for (final Slot slotToSave : slots)
            savedSlots.add(slotRepository.save(slotToSave));

        return savedSlots;
    }

    private List<Slot> checkCollision(final List<Slot> databaseSlots, final List<Slot> slots) {

        final Set<Slot> collisionUniqueSlots = new HashSet<>();

        for(final Slot databaseSlot : databaseSlots){
            for(final Slot slot : slots){
                //collision exists
                if(databaseSlot.getStartTime().isBefore(slot.getFinishTime()) &&
                        databaseSlot.getFinishTime().isAfter(slot.getStartTime())
                ){
                    collisionUniqueSlots.add(slot);
                }
            }
        }

        final List<Slot> collisionSlots = new ArrayList<>(collisionUniqueSlots);
        Collections.sort(collisionSlots);
        return collisionSlots;
    }

    public void deleteSlot(final long slotId, final Principal principal) {

        //find slot
        final Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ExistsException("slot","slot with id " + slotId + " was not found"));

        //find user
        final User user = userServiceProxy.getUserByEmail(principal.getName());

        //find resource
        final Resource resource = resourceRepository.findById(slot.getResourceId())
                .orElseThrow(()->new ExistsException("resource","resource with id " + slot.getResourceId() + " was not found"));

        //check if owner when resource role
        if(user.getUserRole().equals(UserRole.ROLE_RESOURCE) && !resource.getUserId().equals(user.getId())){
            throw new ExistsException("slot", "not owner");
        }

        //delete booking if exists
        if (slot.getIsBooked()){
            bookingServiceProxy.deleteBookingBySlotId(slot.getId());
        }

        slotRepository.deleteById(slotId);
    }

    public Slot getSlot(final long slotId) {
        return slotRepository.findById(slotId)
                .orElseThrow(() -> new ExistsException("slot", "slot with id " + slotId + " was not found"));
    }

    public List<Slot> getResourceSlots(final long resourceId) {
        return slotRepository.findAllByResourceId(resourceId);
    }

    public Slot updateSlot(final boolean isBooked, final long id) {
        //check if exists
        final Slot slot = slotRepository.findById(id)
                .orElseThrow( () -> new ExistsException("slot", "slot with id " + id + " was not found"));

        slot.setIsBooked(isBooked);
        return slotRepository.save(slot);
    }

    void sendNotificationMail(final String userMail) throws javax.mail.MessagingException {

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(userMail);
        mimeMessageHelper.setSubject("Mail notification!");

        final String message = "<head>" +
                "<style type=\"text/css\">" +
                ".red { color: #f00; }" +
                "</style>" +
                "</head>" +
                "<h1 class=\"red\">Booking reminder \uD83D\uDE00</h1>" +
                "<p>" +
                "The time of your booking is approaching." +
                "</p>";

        mimeMessage.setContent(message, "text/html; charset=utf-8");
        javaMailSender.send(mimeMessage);
    }

    @Scheduled(fixedRate = 60000) //minute
    public void sendNotification() throws MessagingException {

        final List<Slot> bookedSlots = slotRepository.findAllByIsBooked(true);

        final LocalDate currentDate = LocalDate.now();
        final LocalDate maxDate = LocalDate.now().plusDays(1);

        for(final Slot slot : bookedSlots) {
            if((slot.getStartTime().toLocalDate().isEqual(currentDate) || slot.getStartTime().toLocalDate().isEqual(maxDate))
                    && slot.getNotificationIsSend().equals(false)){
                final Booking booking = bookingServiceProxy.getBookingBySlotId(slot.getId());
                if(booking.getUser().getEmail() != null) {
                    sendNotificationMail(booking.getUser().getEmail());
                }
                slot.setNotificationIsSend(true);
                slotRepository.save(slot);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void removeOldSlots() {

        final LocalDateTime currentTime = LocalDateTime.now().plusHours(1);

        final List<Slot> slots = (List<Slot>) slotRepository.findAll();

        for(final Slot slot : slots){
            if(slot.getFinishTime().isBefore(currentTime)){
                if(slot.getIsBooked().equals(true)){
                    bookingServiceProxy.deleteBookingBySlotIdScheduling(slot.getId());
                }
                slotRepository.deleteById(slot.getId());
            }
        }
    }

    public List<Slot> getSlots() {
        return (List<Slot>) slotRepository.findAll();
    }
}
