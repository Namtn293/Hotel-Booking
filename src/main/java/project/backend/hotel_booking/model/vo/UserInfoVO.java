package project.backend.hotel_booking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.enumration.UserStatusEnum;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoVO {
    private String userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Long reserveTotal;
    private UserStatusEnum statusEnum;
}
