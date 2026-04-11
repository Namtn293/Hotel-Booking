package project.backend.hotel_booking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelStatisticVO {
    private String hotelName;
    private LocalDate createdDate;
}
