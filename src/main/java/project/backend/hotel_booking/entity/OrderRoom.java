package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.core.util.EntityBase;
import project.backend.hotel_booking.enumration.PaymentStatus;
import project.backend.hotel_booking.enumration.QualityEnum;

import java.time.LocalDate;

@Entity(name = "MAIN_ROOM_ORDER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRoom extends EntityBase {
    private String userName;

    private Long hotelId;

    private Long roomId;

    private Long capacity;

    private QualityEnum qualityEnum;

    private double price;

    private double returnPrice;

    private LocalDate orderDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private PaymentStatus paymentStatus;

    public void prePersist(){
        this.orderDate = LocalDate.now();
    }
}
