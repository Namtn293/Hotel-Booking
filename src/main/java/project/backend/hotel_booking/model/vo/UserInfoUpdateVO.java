package project.backend.hotel_booking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdateVO {
    private String fullName;
    private String phoneNumber;
    private String accountNumber;
    private LocalDate birthDay;
    private String email;
}
