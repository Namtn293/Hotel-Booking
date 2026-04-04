package project.backend.hotel_booking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.enumration.ActiveStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelInfoVO {
    private String hotelId;
    private String hotelName;
    private String partnerName;
    private Long roomTotal;
    private double revenue;
    private ActiveStatus status;
}
