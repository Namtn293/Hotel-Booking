package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import project.backend.hotel_booking.core.util.EntityBase;

import java.time.LocalDateTime;

@Entity
public class Notifications extends EntityBase {
    private LocalDateTime createdDate;

    private String userId;

    private String content;
}
