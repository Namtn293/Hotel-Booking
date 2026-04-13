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
public class OrderRoomUserVO {
    private Long id;

    private String roomName;

    private String hotelName;

    private String address;

    private QualityEnum qualityEnum;

    private double price;

    private LocalDate startDate;

    private LocalDate endDate;

    private PaymentStatus paymentStatus;

    private Long roomId;
}
