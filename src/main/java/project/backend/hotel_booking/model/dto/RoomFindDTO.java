package project.backend.hotel_booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomFindDTO {
    private String content;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Long capacity;
}
