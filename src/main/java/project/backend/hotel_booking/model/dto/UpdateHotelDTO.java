package project.backend.hotel_booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.HotelEnum;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHotelDTO {
    private String hotelName;

    private String description;

    private String email;

    private String phoneNumber;

    private ActiveStatus activeStatus;

    private String address;
}
