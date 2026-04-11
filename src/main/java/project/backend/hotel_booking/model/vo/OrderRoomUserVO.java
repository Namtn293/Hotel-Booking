package project.backend.hotel_booking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.enumration.QualityEnum;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRoomUserVO {
    private String hotelName;

    private int capacity;

    private QualityEnum qualityEnum;

    private double price;

    private LocalDate orderDate;

    private LocalDate startDate;

    private LocalDate endDate;
}
