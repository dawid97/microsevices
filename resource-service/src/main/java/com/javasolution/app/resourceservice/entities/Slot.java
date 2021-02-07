package com.javasolution.app.resourceservice.entities;

import com.javasolution.app.resourceservice.proxies.BookingServiceProxy;
import lombok.Data;;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "Slots")
public class Slot implements Comparable<Slot>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime finishTime;

    private Boolean isBooked;

    private Boolean notificationIsSend;

    @Transient
    private Long divisor;

    private Long resourceId;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.isBooked = false;
        this.notificationIsSend = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    @Override
    public int compareTo(Slot o) {
        return getStartTime().compareTo(o.getStartTime());
    }
}
