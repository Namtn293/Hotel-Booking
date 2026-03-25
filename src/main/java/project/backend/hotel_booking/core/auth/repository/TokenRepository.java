package project.backend.hotel_booking.core.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.core.auth.entity.Token;

import java.util.List;
import java.util.Optional;
@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    List<Token> findByUserId(Long userId);

    Optional<Token> findByToken(String token);
}
