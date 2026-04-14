package project.backend.hotel_booking.service;

import project.backend.hotel_booking.model.dto.UserInfoUpdateDTO;
import project.backend.hotel_booking.model.vo.UserInfoUpdateVO;
import project.backend.hotel_booking.model.vo.UserInfoVO;

import java.util.List;

public interface UserInfoService {
    List<UserInfoVO> getAllUserInfo();

    void lockUser(String userId);

    Long accountCount();

    UserInfoUpdateVO updateInfo(UserInfoUpdateDTO userInfoUpdateDTO);
}
