package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.entity.Notification;
import project.backend.hotel_booking.model.vo.NotificationVO;
import project.backend.hotel_booking.repository.NotificationRepository;
import project.backend.hotel_booking.repository.UserInfoRepository;
import project.backend.hotel_booking.service.NotificationService;

import java.util.List;

@Service
public class NotificationServiceImplement implements NotificationService {
    private final UserInfoRepository userInfoRepository;
    private final NotificationRepository notificationRepository;

    public NotificationServiceImplement(UserInfoRepository userInfoRepository, NotificationRepository notificationRepository) {
        this.userInfoRepository = userInfoRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<NotificationVO> getMyNotification() {
        Long userId=userInfoRepository.getUserInfoIdByUserName(ThreadContext.getUserDetail().getUsername());
        return notificationRepository.getAllNotifications(userId);
    }

    @Override
    public void createNotification(String content) {
        notificationRepository.save(Notification.builder()
                        .content(content)
                        .userId(userInfoRepository.getUserInfoIdByUserName(ThreadContext.getUserDetail().getUsername()))
                .build());
    }
}
