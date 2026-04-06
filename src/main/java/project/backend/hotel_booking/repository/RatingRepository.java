package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.Rating;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {

    boolean existsByUserIdAndHotelId(Long userId,Long hotelId);

    List<Rating> getRatingByHotelId(Long hotelId);
}
