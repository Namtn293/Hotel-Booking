package project.backend.hotel_booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.core.util.EntityBase;
import project.backend.hotel_booking.enumration.PartnerStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
public class PartnerInfo extends EntityBase {

    @Column(name = "PARTNER_NAME")
    private String partnerName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phonNumber;

    @Column(name = "ADDRESS")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private PartnerStatus partnerStatus;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

    @PrePersist
    public void prePersist(){
        this.createdDate=LocalDate.now();
    }
}
