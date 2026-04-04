package project.backend.hotel_booking.core.auth.model.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String fullName;
    private String userName;
    private String password;
    private String address;
    private String phoneNumber;
    private String rePassword;
}
