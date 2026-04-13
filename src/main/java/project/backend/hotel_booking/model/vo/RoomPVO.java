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
public class RoomPVO {
    private Long roomId;
    private String roomName;
    private String url;
    private QualityEnum qualityEnum;
    private Double price;
    private Long hotelId;
    private String hotelName;
    private String description;
    private Long capacity;
    private ActiveStatus activeStatus;
}
