package project.backend.hotel_booking.model.dto;

import lombok.*;
import project.backend.hotel_booking.enumration.QualityEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateDTO {
    private String roomName;
    private Long capacity;
    private double price;
    private QualityEnum qualityEnum;
}
