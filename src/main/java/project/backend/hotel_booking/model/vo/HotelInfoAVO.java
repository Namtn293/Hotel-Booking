package project.backend.hotel_booking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.enumration.HotelEnum;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelInfoAVO {
    private Long id;
    private String hotelName;
    private String partnerName;
    private String email;
    private LocalDate createdDate;
    private HotelEnum status;
}
