package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import project.backend.hotel_booking.core.util.EntityBase;
import project.backend.hotel_booking.enumration.FormStatusEnum;

import java.time.LocalDateTime;

@Entity
public class BookingForm extends EntityBase {
    private Long userId;

    private Long roomId;

    private Double totalAmount;

    private Double voucher;

    private FormStatusEnum formStatusEnum;

    private LocalDateTime startDay;

    private LocalDateTime endDay;
}
