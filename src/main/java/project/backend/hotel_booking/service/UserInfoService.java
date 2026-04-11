package project.backend.hotel_booking.service;

import project.backend.hotel_booking.model.vo.UserInfoVO;

import java.util.List;

public interface UserInfoService {
    List<UserInfoVO> getAllUserInfo();

    void lockUser(String userId);

    Long accountCount();
}
