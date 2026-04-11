package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.backend.hotel_booking.entity.OrderRoom;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRoomRepository extends JpaRepository<OrderRoom,Long> {
    @Override
    Optional<OrderRoom> findById(Long id);

    List<OrderRoom> findAllByHotelId(Long hotelId);

    List<OrderRoom> findAllByUserName(String userName);

    @Query(value = "select sum(a.price)" +
            "from MAIN_ROOM_ORDER a " +
            "where a.orderDate=:today")
    Double getTodayRevenue(@Param("today") LocalDate createdDate);


    @Query(value = "select count(a) " +
            "from MAIN_ROOM_ORDER a " +
            "where a.orderDate=:today")
    Long getTodayReversedOrder(@Param("today") LocalDate createdDate);
}
