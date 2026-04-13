package project.backend.hotel_booking.model.dto;

import lombok.Data;

@Data
public class PartnerInfoUpdateDTO {
    private Long userId;
    private String email;
    private String phoneNumber;
    private String partnerName;
    private String address;
}
