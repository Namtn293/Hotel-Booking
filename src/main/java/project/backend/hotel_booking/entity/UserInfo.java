package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.core.util.EntityBase;
import project.backend.hotel_booking.enumration.UserStatusEnum;

import java.time.LocalDate;

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

    private String avatar;

    private LocalDate createdDate;

    @PrePersist
    public void prePersist(){
        this.createdDate=LocalDate.now();
    }
}
