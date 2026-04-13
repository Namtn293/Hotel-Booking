package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.auth.entity.User;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.PartnerInfo;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.enumration.PartnerStatus;
import project.backend.hotel_booking.model.dto.PartnerInfoUpdateDTO;
import project.backend.hotel_booking.model.vo.PartnerInfoGetVO;
import project.backend.hotel_booking.model.vo.PartnerInfoManageVO;
import project.backend.hotel_booking.model.vo.PartnerInfoVO;
import project.backend.hotel_booking.repository.PartnerInfoRepository;
import project.backend.hotel_booking.service.NotificationService;
import project.backend.hotel_booking.service.PartnerInfoService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PartnerInfoServiceImplement implements PartnerInfoService {
    private final PartnerInfoRepository partnerInfoRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Autowired
    public PartnerInfoServiceImplement(UserRepository userRepository,NotificationService notificationService,PartnerInfoRepository partnerInfoRepository) {
        this.partnerInfoRepository = partnerInfoRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
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
            notificationService.createNotification("Bỏ cấm thành công đối tác "+partnerId);
        } else {
            notificationService.createNotification("Cấm thành công đối tác "+partnerId);
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

    @Override
    public String updatePartnerInfo(PartnerInfoUpdateDTO partnerInfoUpdateDTO) {
        PartnerInfo partnerInfo=partnerInfoRepository.getPartnerInfoByUserId(partnerInfoUpdateDTO.getUserId()).orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_ALREADY_EXIST));
        if (partnerInfoUpdateDTO.getPartnerName()!=null){
            partnerInfo.setPartnerName(partnerInfoUpdateDTO.getPartnerName());
        }
        if (partnerInfoUpdateDTO.getEmail()!=null){
            partnerInfo.setPartnerName(partnerInfoUpdateDTO.getEmail());
        }
        if (partnerInfoUpdateDTO.getAddress()!=null){
            partnerInfo.setPartnerName(partnerInfoUpdateDTO.getAddress());
        }
        if (partnerInfoUpdateDTO.getPhoneNumber()!=null){
            partnerInfo.setPartnerName(partnerInfoUpdateDTO.getPhoneNumber());
        }
        partnerInfoRepository.save(partnerInfo);
        return "Update Success";
    }

    @Override
    public PartnerInfoGetVO getPartnerInfo() {
        User user=userRepository.findByUserName(ThreadContext.getUserDetail().getUsername()).orElseThrow(()->new BusinessException(ErrorCode.PARTNER_NOT_ALREADY_EXIST));
        PartnerInfo partnerInfo= partnerInfoRepository.getPartnerInfoByUserId(user.getId()).orElseThrow(()->new BusinessException(ErrorCode.PARTNER_NOT_ALREADY_EXIST));
        return PartnerInfoGetVO.builder()
                .address(partnerInfo.getAddress())
                .phoneNumber(partnerInfo.getPhonNumber())
                .email(partnerInfo.getEmail())
                .partnerName(partnerInfo.getPartnerName())
                .build();
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
