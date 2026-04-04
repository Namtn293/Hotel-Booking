package project.backend.hotel_booking.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelInfoDTO {
    private Long id;
    private String hotelName;
    private String address;
}
