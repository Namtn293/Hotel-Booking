package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.Hotel;

import java.util.List;

@Repository
public interface HotelsRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findAllByPartnerId(Long partnerId);
}
