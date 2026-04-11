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
public class RatingVO {
    private String avatar;
    private String userName;
    private Long rating;
    private String reason;
    private String createdDate;
    private String url;
}
