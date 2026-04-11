package project.backend.hotel_booking.service;

import project.backend.hotel_booking.model.vo.NotificationVO;

import java.util.List;

public interface NotificationService {
    List<NotificationVO> getMyNotification();
    void createNotification(String content);
}
