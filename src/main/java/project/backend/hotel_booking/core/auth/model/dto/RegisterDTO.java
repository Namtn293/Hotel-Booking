package project.backend.hotel_booking.core.auth.model.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String fullName;
    private String email;
    private String userName;
    private String password;
}
