package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.Hotel;

import java.util.List;

@Repository
public interface HotelsRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findAllByPartnerId(Long partnerId);

    @Query("SELECT h.hotelName "+
    "FROM Hotel h "+
    "WHERE h.id= :hotelId")
    String getHotelNameById(@Param("hotelId") Long hotelId);
}
