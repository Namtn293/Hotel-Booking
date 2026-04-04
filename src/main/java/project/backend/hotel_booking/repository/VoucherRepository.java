package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.Voucher;
@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Long> {
}
