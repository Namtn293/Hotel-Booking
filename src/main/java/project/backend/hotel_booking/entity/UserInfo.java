package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import project.backend.hotel_booking.core.util.EntityBase;
import project.backend.hotel_booking.enumration.UserStatusEnum;

@Entity
public class UserInfo extends EntityBase {
    private String fullName;

    private String address;

    private String email;

    private UserStatusEnum userStatusEnum;

    private Long userId;
}
