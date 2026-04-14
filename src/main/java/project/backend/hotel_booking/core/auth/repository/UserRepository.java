package project.backend.hotel_booking.core.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.core.auth.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUserName(String userName);

    Optional<User> findByUserName(String userName);
    @Query(value = "Select u.id from User u where u.userName=:userName")
    Optional<Long> findIdByUserName(String userName);

    @Query(value = "select a.userName " +
            "from User a " +
            "where a.id=:id")
    String findUserNameById(@Param("id")Long id);
}
