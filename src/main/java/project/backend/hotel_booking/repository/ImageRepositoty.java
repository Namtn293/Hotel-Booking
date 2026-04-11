package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.Image;

import java.util.Optional;

@Repository
public interface ImageRepositoty extends JpaRepository<Image,Long> {
    Optional<Image> findById(Long id);
}
