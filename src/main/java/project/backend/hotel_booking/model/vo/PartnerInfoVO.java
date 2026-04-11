package project.backend.hotel_booking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PartnerInfoVO {
    private String partnerName;
    private LocalDate sendDate;
    private String partnerId;
    private String email;
    private String phoneNumber;
}
