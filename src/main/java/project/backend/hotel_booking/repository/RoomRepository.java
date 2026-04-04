package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.Room;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    @Override
    Optional<Room> findById(Long roomId);
    List<Room> findAllByHotelId(Long hotelId);
}
