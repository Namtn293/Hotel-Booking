package project.backend.hotel_booking.core.auth.model.dto;

import lombok.Data;

@Data
public class ChangePassword {
    private Long userId;
    private String password;
    private String newPassword;
    private String reNewPassword;
}
