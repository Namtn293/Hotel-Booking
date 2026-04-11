package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.Hotel;
import project.backend.hotel_booking.model.vo.HotelStatisticVO;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelsRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findAllByPartnerId(Long partnerId);

    @Query("SELECT h.hotelName "+
    "FROM Hotel h "+
    "WHERE h.id= :hotelId")
    String getHotelNameById(@Param("hotelId") Long hotelId);

    @Query(value = "select * from hotel " +
            "where hotel_enum=2 " +
            " limit 5 ", nativeQuery = true)
    List<Hotel> getTop5Hotel();


    @Query(value = "select count(a)" +
            "from Hotel a " +
            "where a.createdDate=:date and a.hotelEnum=2")
    Long getNewHotelPending(@Param("date")LocalDate createdDate);
}
