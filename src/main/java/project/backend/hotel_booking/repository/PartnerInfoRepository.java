package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.PartnerInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerInfoRepository extends JpaRepository<PartnerInfo,Long> {

    @Query(value = "select a from PartnerInfo a " +
            "where a.partnerStatus=ACTIVE ")
    List<PartnerInfo> getAllPartnerPending();

    Optional<PartnerInfo> findByUserId(Long userId);

    @Query(value = "Select a.partnerName from PartnerInfo a where a.id=:id")
    Optional<String> findPartnerNameById(Long id);
}
