package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import project.backend.hotel_booking.core.util.EntityBase;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.QualityEnum;

@Entity
public class Room extends EntityBase {
    private String roomName;

    private Long capacity;

    private ActiveStatus activeStatus;

    private double price;

    private Long hotelId;

    private QualityEnum qualityEnum;
}
