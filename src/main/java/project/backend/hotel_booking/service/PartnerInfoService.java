package project.backend.hotel_booking.service;

import project.backend.hotel_booking.model.vo.PartnerInfoManageVO;
import project.backend.hotel_booking.model.vo.PartnerInfoVO;

import java.util.List;

public interface PartnerInfoService {
    List<PartnerInfoVO> getAllPartnerPending();

    void rejectPartner(String partnerId);

    void bandPartner(Long partnerId);

    List<PartnerInfoManageVO> gerAllPartnerInfo();
}
