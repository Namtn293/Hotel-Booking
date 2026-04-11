package project.backend.hotel_booking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.enumration.PartnerStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerInfoManageVO {
    private Long partnerId;
    private String partnerName;
    private String email;
    private String phoneNumber;
    private PartnerStatus partnerStatus;
    private Double revenueTotal;
}
