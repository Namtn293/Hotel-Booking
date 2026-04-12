package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.PartnerInfo;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.enumration.PartnerStatus;
import project.backend.hotel_booking.model.vo.PartnerInfoManageVO;
import project.backend.hotel_booking.model.vo.PartnerInfoVO;
import project.backend.hotel_booking.repository.PartnerInfoRepository;
import project.backend.hotel_booking.service.PartnerInfoService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PartnerInfoServiceImplement implements PartnerInfoService {
    private final PartnerInfoRepository partnerInfoRepository;

    @Autowired
    public PartnerInfoServiceImplement(PartnerInfoRepository partnerInfoRepository) {
        this.partnerInfoRepository = partnerInfoRepository;
    }

    @Override
    public List<PartnerInfoVO> getAllPartnerPending() {
        List<PartnerInfo> list=partnerInfoRepository.getAllPartnerPending();
        return convertToPartnerVO(list);
    }

    @Override
    public void rejectPartner(String partnerId) {
        Long id=Long.parseLong(partnerId.trim().substring(2));
        PartnerInfo partnerInfo=partnerInfoRepository.findById(id).orElseThrow(()-> new BusinessException(ErrorCode.PARTNER_NOT_ALREADY_EXIST));
        partnerInfo.setPartnerStatus(PartnerStatus.REJECT);
        partnerInfoRepository.save(partnerInfo);
    }

    @Override
    public void bandPartner(Long partnerId) {
        PartnerInfo partnerInfo=partnerInfoRepository.findById(partnerId).orElseThrow(()-> new BusinessException(ErrorCode.PARTNER_NOT_ALREADY_EXIST));
        if(partnerInfo.getPartnerStatus().equals(PartnerStatus.BANDING)){
            partnerInfo.setPartnerStatus(PartnerStatus.ACTIVE);
        } else {
            partnerInfo.setPartnerStatus(PartnerStatus.BANDING);
        }
        partnerInfoRepository.save(partnerInfo);
    }

    @Override
    public List<PartnerInfoManageVO> gerAllPartnerInfo() {
        LocalDate date=LocalDate.now();
        long month=date.getMonthValue();
        return partnerInfoRepository.getPartnerInfoAdmin(month);
    }


    public List<PartnerInfoVO> convertToPartnerVO(List<PartnerInfo> list){
        List<PartnerInfoVO> listVo=new ArrayList<>();
        list.forEach(c->{
            PartnerInfoVO vo=PartnerInfoVO.builder()
                    .partnerId("DN"+String.format("%03d",c.getId()))
                    .sendDate(c.getCreatedDate())
                    .partnerName(c.getPartnerName())
                    .email(c.getEmail())
                    .phoneNumber(c.getPhonNumber())
                    .build();
            listVo.add(vo);
        });
        return listVo;
    }

}
