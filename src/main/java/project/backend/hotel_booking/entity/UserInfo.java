package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.core.util.EntityBase;
import project.backend.hotel_booking.enumration.UserStatusEnum;
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo extends EntityBase {
    private String fullName;

    private String phoneNumber;

    private String address;

    private UserStatusEnum userStatusEnum;

    private String email;

    private Long userId;
}
