package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.auth.entity.User;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.UserInfo;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.enumration.UserStatusEnum;
import project.backend.hotel_booking.model.dto.UserInfoUpdateDTO;
import project.backend.hotel_booking.model.vo.UserInfoUpdateVO;
import project.backend.hotel_booking.model.vo.UserInfoVO;
import project.backend.hotel_booking.repository.HotelsRepository;
import project.backend.hotel_booking.repository.OrderRoomRepository;
import project.backend.hotel_booking.repository.UserInfoRepository;
import project.backend.hotel_booking.service.NotificationService;
import project.backend.hotel_booking.service.UserInfoService;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserInfoServiceImplement implements UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final OrderRoomRepository orderRoomRepository;
    public UserInfoServiceImplement(OrderRoomRepository orderRoomRepository,UserRepository userRepository,UserInfoRepository userInfoRepository,NotificationService notificationService) {
        this.userInfoRepository = userInfoRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
        this.orderRoomRepository = orderRoomRepository;
    }

    @Override
    public List<UserInfoVO> getAllUserInfo() {
        List<UserInfo> list=userInfoRepository.findAll();
        List<UserInfoVO> listVo=new ArrayList<>();
        list.forEach(c->{
            String userName=userRepository.findUserNameById(c.getUserId());
            UserInfoVO userInfoVO=UserInfoVO.builder()
                    .statusEnum(c.getUserStatusEnum())
                    .phoneNumber(c.getPhoneNumber())
                    .email(c.getEmail())
                    .reserveTotal(orderRoomRepository.countTotalReserve(userName))
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

    @Override
    public UserInfoUpdateVO updateInfo(UserInfoUpdateDTO userInfoUpdateDTO) {
        UserInfo userInfo = userInfoRepository.findById(userInfoRepository.getUserInfoIdByUserName(ThreadContext.getUserDetail().getUsername())).orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        userInfo.setEmail(userInfoUpdateDTO.getEmail());
        userInfo.setFullName(userInfoUpdateDTO.getFullName());
        userInfo.setAccountNumber(userInfoUpdateDTO.getAccountNumber());
        userInfo.setPhoneNumber(userInfoUpdateDTO.getPhoneNumber());
        userInfo.setBirthDay(userInfoUpdateDTO.getBirthDay());
        return UserInfoUpdateVO.builder()
                .accountNumber(userInfo.getAccountNumber())
                .birthDay(userInfo.getBirthDay())
                .email(userInfo.getEmail())
                .fullName(userInfo.getFullName())
                .phoneNumber(userInfo.getPhoneNumber())
                .build();
    }

    @Override
    public UserInfoUpdateVO getUserInfo() {
        UserInfo userInfo = userInfoRepository.findById(userInfoRepository.getUserInfoIdByUserName(ThreadContext.getUserDetail().getUsername())).orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserInfoUpdateVO.builder()
                .accountNumber(userInfo.getAccountNumber())
                .birthDay(userInfo.getBirthDay())
                .email(userInfo.getEmail())
                .fullName(userInfo.getFullName())
                .phoneNumber(userInfo.getPhoneNumber())
                .build();
    }
}
