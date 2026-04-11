package project.backend.hotel_booking.model.dto;

import lombok.Data;

@Data
public class RatingDTO {
    private Long rating;
    private String reason;
    private Long hotelId;
}
