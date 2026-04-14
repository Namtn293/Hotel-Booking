package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.UserInfo;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> getUserInfoByUserId(Long userId);

    Long countUserInfoByCreatedDate(LocalDate createdDate);

    Optional<UserInfo> getByUserId(Long userId);

    @Query(value = "select a.id " +
            "from UserInfo a " +
            "join User b on a.userId=b.id " +
            "where b.userName=:user_name ")
    Long getUserInfoIdByUserName(@Param("user_name")String userName);

    boolean existsByEmail(String email);
}
