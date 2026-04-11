package project.backend.hotel_booking.core.auth.model.dto;

import lombok.Data;

@Data
public class RegisterPartnerDTO {
    private String userName;
    private String password;
    private String rePassword;
    private String partnerName;
    private String email;
    private String phoneNumber;
    private String address;
}
