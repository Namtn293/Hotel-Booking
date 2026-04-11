package project.backend.hotel_booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.enumration.ActiveStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelRegisterDTO {
    private String hotelName;
    private String address;
    private String description;
}
