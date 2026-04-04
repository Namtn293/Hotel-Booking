package project.backend.hotel_booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import project.backend.hotel_booking.core.util.EntityBase;
import project.backend.hotel_booking.enumration.VoucherEnum;

@Entity
public class Voucher extends EntityBase {
    @Column(name = "HOTEL_ID")
    private String hotelId;

    @Column(name = "VALUE")
    private double value;

    @Column(name = "VOUCHER_TYPE")
    private VoucherEnum voucherType;
}