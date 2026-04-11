package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.hotel_booking.entity.OrderRoom;

import java.util.List;
import java.util.Optional;

public interface OrderRoomRepository extends JpaRepository<OrderRoom,Long> {
    @Override
    Optional<OrderRoom> findById(Long id);

    List<OrderRoom> findAllByHotelId(Long hotelId);
    List<OrderRoom> findAllByUserName(String userName);
    void deleteAllByHotelId(Long hotelId);
}
