package project.backend.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.hotel_booking.entity.PartnerInfo;
import project.backend.hotel_booking.model.vo.PartnerInfoManageVO;

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

    @Query(value = "select new project.backend.hotel_booking.model.vo.PartnerInfoManageVO(a.id,a.partnerName,a.email,a.phonNumber,a.partnerStatus,cast(coalesce(sum(c.price),0.0d)as double )) " +
            "from PartnerInfo a " +
            "left join Hotel b on a.id=b.partnerId " +
            "left join MAIN_ROOM_ORDER c on c.hotelId=b.id and c.paymentStatus=2 " +
            "and month(c.orderDate) = :month " +
            "group by a.id,a.partnerName,a.email,a.phonNumber,a.partnerStatus")
    List<PartnerInfoManageVO> getPartnerInfoAdmin(@Param("month") Long month);

    @Query(value = "select a.id " +
            "from PartnerInfo a " +
            "join User b on a.userId=b.id and b.userName=:user_name ")
    Long getPartnerInfo(@Param("user_name")String userName);

    Optional<PartnerInfo> getPartnerInfoByUserId(Long userId);

    boolean existsByEmail(String email);
}
