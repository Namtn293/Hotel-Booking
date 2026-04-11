package project.backend.hotel_booking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.enumration.PaymentStatus;
import project.backend.hotel_booking.enumration.QualityEnum;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRoomPartnerVO {
    private String userName;

    private String roomName;

    private Long capacity;

    private QualityEnum qualityEnum;

    private double price;

    private LocalDate orderDate;

    private PaymentStatus paymentStatus;
}
