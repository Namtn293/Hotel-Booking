package project.backend.hotel_booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import project.backend.hotel_booking.core.util.EntityBase;
import project.backend.hotel_booking.enumration.PartnerStatus;

@Entity
public class PartnerInfo extends EntityBase {
    @Column(name = "COMPANY_ID")
    private String companyId;

    @Column(name = "PARTNER_NAME")
    private String partnerName;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phonNumber;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "STATUS")
    private PartnerStatus partnerStatus;

    private Long userId;
}
