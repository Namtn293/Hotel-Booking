package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.UserInfo;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> getUserInfoByUserId(Long userId);

    Long countUserInfoByCreatedDate(LocalDate createdDate);
}
