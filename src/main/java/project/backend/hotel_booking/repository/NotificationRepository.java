package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.Notification;
import project.backend.hotel_booking.model.vo.NotificationVO;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Query(value = " select new project.backend.hotel_booking.model.vo.NotificationVO(a.content,a.createdTime)" +
            " from Notification a " +
            " where a.userId=:user_id " +
            " order by a.createdTime asc ")
    List<NotificationVO> getAllNotifications(@Param("user_id") Long userId);
}
