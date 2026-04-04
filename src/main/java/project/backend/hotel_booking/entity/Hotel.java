package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import project.backend.hotel_booking.core.util.EntityBase;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.HotelEnum;

import java.time.LocalDate;

@Entity
public class Hotel extends EntityBase {
    private String hotelName;

    private Long partnerId;

    private String description;

    private String email;

    private String phoneNumber;

    private LocalDate createdDate;

    private HotelEnum hotelEnum;

    private ActiveStatus activeStatus;

    @PrePersist
    public void prePersist(){
        this.createdDate=LocalDate.now();
    }
}
