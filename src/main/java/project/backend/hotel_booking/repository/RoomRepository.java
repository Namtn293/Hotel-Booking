package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.Room;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.PaymentStatus;
import project.backend.hotel_booking.model.vo.RoomVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    @Override
    Optional<Room> findById(Long roomId);
    List<Room> findAllByHotelId(Long hotelId);
    void deleteAllByHotelId(Long hotelId);

    @Query("""
    SELECT new project.backend.hotel_booking.model.vo.RoomVO(
        r.id,
        r.roomName,
        r.capacity,
        r.activeStatus,
        r.price,
        r.qualityEnum,
        r.hotelId,
        h.hotelName,
        h.address,
        COUNT(rt.id),
        AVG(rt.rating),
        i.url,
        r.description
    )
    FROM MAIN_ROOM r
    LEFT JOIN Hotel h ON h.id = r.hotelId
    LEFT JOIN MAIN_IMAGE i ON i.id = r.imageId
    LEFT JOIN Rating rt ON rt.roomId = r.id
    WHERE r.id = :roomId
    GROUP BY
        r.id,
        r.roomName,
        r.capacity,
        r.activeStatus,
        r.price,
        r.qualityEnum,
        r.hotelId,
        h.hotelName,
        h.address,
        i.url,
        r.description
""")
    Optional<RoomVO> getRoomByRoomId(@Param("roomId") Long roomId);

    @Query("""
    SELECT r
    FROM MAIN_ROOM r
    WHERE r.hotelId IN :hotelIds
    AND r.activeStatus = :status
    AND NOT EXISTS (
        SELECT 1
        FROM MAIN_ROOM_ORDER o
        WHERE o.roomId = r.id
        AND o.startDate <= :checkOut
        AND o.endDate >= :checkIn
        AND (o.paymentStatus = :paymentStatus
        OR o.paymentStatus = NULL)
    )
""")
    List<Room> findAvailableRooms(
            @Param("hotelIds") List<Long> hotelIds,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("status") ActiveStatus status,
            @Param("paymentStatus")PaymentStatus paymentStatus
    );

    @Query(value = "select a.roomName " +
            "from MAIN_ROOM a " +
            "where a.id=:id")
    String getRoomNameById(@Param("id") Long id);


}
