package project.backend.hotel_booking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.QualityEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomVO {
    private String roomName;
    private Long capacity;
    private ActiveStatus activeStatus;
    private double price;
    private QualityEnum qualityEnum;
    private Long hotelId;
}
