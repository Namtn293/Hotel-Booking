package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.backend.hotel_booking.entity.OrderRoom;
import project.backend.hotel_booking.model.vo.OrderRoomDataVO;

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
            "where a.orderDate=:today and a.paymentStatus=1")
    Double getTodayRevenue(@Param("today") LocalDate createdDate);


    @Query(value = "select count(a) " +
            "from MAIN_ROOM_ORDER a " +
            "where a.orderDate=:today")
    Long getTodayReversedOrder(@Param("today") LocalDate createdDate);


    @Query(value = " select new project.backend.hotel_booking.model.vo.OrderRoomDataVO(a.orderDate,sum(a.price)) " +
            "from MAIN_ROOM_ORDER a " +
            "where a.paymentStatus=1 and a.orderDate between :start and :end " +
            "group by a.orderDate " +
            "order by a.orderDate asc")
    List<OrderRoomDataVO> getRevenueTotal(@Param("start")LocalDate start,@Param("end")LocalDate end);
}
