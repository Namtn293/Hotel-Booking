package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.Notification;
import project.backend.hotel_booking.entity.PartnerInfo;
import project.backend.hotel_booking.entity.UserInfo;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.model.vo.NotificationVO;
import project.backend.hotel_booking.repository.NotificationRepository;
import project.backend.hotel_booking.repository.PartnerInfoRepository;
import project.backend.hotel_booking.repository.UserInfoRepository;
import project.backend.hotel_booking.service.NotificationService;

import java.util.List;

@Service
public class NotificationServiceImplement implements NotificationService {
    private final UserInfoRepository userInfoRepository;
    private final NotificationRepository notificationRepository;
    private final PartnerInfoRepository partnerInfoRepository;
    private final UserRepository userRepository;

    public NotificationServiceImplement(UserRepository userRepository,PartnerInfoRepository partnerInfoRepository,UserInfoRepository userInfoRepository, NotificationRepository notificationRepository) {
        this.userInfoRepository = userInfoRepository;
        this.notificationRepository = notificationRepository;
        this.partnerInfoRepository = partnerInfoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<NotificationVO> getMyNotification() {
        Long userId=userRepository.findIdByUserName(ThreadContext.getUserDetail().getUsername()).orElse(null);
        return notificationRepository.getAllNotifications(userId);
    }

    @Override
    public void createNotification(String content) {
        notificationRepository.save(Notification.builder()
                        .content(content)
                        .userId(userRepository.findIdByUserName(ThreadContext.getUserDetail().getUsername()).orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_ALREADY_EXIST)))
                .build());
    }
}
