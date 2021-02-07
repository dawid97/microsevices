package com.javasolution.app.bookingservice.entities;

import com.javasolution.app.bookingservice.responses.Resource;
import com.javasolution.app.bookingservice.responses.Slot;
import com.javasolution.app.bookingservice.responses.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "Bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createAt;

    private Long userId;

    private Long slotId;

    private LocalDateTime updateAt;

    @Transient
    private Slot slot;

    @Transient
    private Resource resource;

    @Transient
    private User user;

    @PrePersist
    protected void onCreate() { this.createAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
