package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.Room;
import project.backend.hotel_booking.model.vo.RoomVO;

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
        r.roomName,
        r.capacity,
        r.activeStatus,
        r.price,
        r.qualityEnum,
        h.hotelName,
        h.address,
        COUNT(rt.id),
        AVG(rt.rating),
        i.url
    )
    FROM MAIN_ROOM r
    LEFT JOIN Hotel h ON h.id = r.hotelId
    LEFT JOIN MAIN_IMAGE i ON i.id = r.imageId
    LEFT JOIN Rating rt ON rt.roomId = r.id
    WHERE r.id = :roomId
    GROUP BY
        r.roomName,
        r.capacity,
        r.activeStatus,
        r.price,
        r.qualityEnum,
        h.hotelName,
        h.address,
        i.url
""")
    Optional<RoomVO> getRoomByRoomId(@Param("roomId") Long roomId);
}
