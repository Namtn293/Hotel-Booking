package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.UserInfo;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.enumration.UserStatusEnum;
import project.backend.hotel_booking.model.vo.UserInfoVO;
import project.backend.hotel_booking.repository.HotelsRepository;
import project.backend.hotel_booking.repository.UserInfoRepository;
import project.backend.hotel_booking.service.NotificationService;
import project.backend.hotel_booking.service.UserInfoService;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserInfoServiceImplement implements UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final NotificationService notificationService;
    public UserInfoServiceImplement(UserInfoRepository userInfoRepository,NotificationService notificationService) {
        this.userInfoRepository = userInfoRepository;
        this.notificationService = notificationService;
    }

    @Override
    public List<UserInfoVO> getAllUserInfo() {
        List<UserInfo> list=userInfoRepository.findAll();
        List<UserInfoVO> listVo=new ArrayList<>();
        list.forEach(c->{
            Long total=0L;
            UserInfoVO userInfoVO=UserInfoVO.builder()
                    .statusEnum(c.getUserStatusEnum())
                    .phoneNumber(c.getPhoneNumber())
                    .email(c.getEmail())
                    .reserveTotal(total)
                    .userId("US"+String.format("%03d",c.getUserId()))
                    .fullName(c.getFullName())
                    .build();
            listVo.add(userInfoVO);
        });
        listVo.sort(Comparator.comparing(UserInfoVO::getUserId));
        return listVo;
    }

    @Override
    public void lockUser(String userId) {
        Long id=Long.parseLong(userId.trim().substring(2));
        UserInfo userInfo=userInfoRepository.getUserInfoByUserId(id).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_ALREADY_EXIST));
        if (Objects.equals(UserStatusEnum.ACTIVE,userInfo.getUserStatusEnum())){
            userInfo.setUserStatusEnum(UserStatusEnum.BANNED);
            notificationService.createNotification("Bạn đã khóa tài khoản "+id);
        } else {
            userInfo.setUserStatusEnum(UserStatusEnum.ACTIVE);
            notificationService.createNotification("Bạn đã mở khóa tài khoản "+id);
        }
        userInfoRepository.save(userInfo);
    }

    @Override
    public Long accountCount() {
        return userInfoRepository.countUserInfoByCreatedDate(LocalDate.now());
    }
}
