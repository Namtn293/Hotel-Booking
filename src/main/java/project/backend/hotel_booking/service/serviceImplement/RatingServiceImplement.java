package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.hotel_booking.core.auth.entity.User;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.Rating;
import project.backend.hotel_booking.entity.UserInfo;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.model.dto.RatingDTO;
import project.backend.hotel_booking.model.dto.RatingGetDTO;
import project.backend.hotel_booking.model.vo.RatingVO;
import project.backend.hotel_booking.repository.HotelsRepository;
import project.backend.hotel_booking.repository.RatingRepository;
import project.backend.hotel_booking.repository.UserInfoRepository;
import project.backend.hotel_booking.service.NotificationService;
import project.backend.hotel_booking.service.RatingService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingServiceImplement implements RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final NotificationService notificationService;
    private final HotelsRepository hotelsRepository;
    public RatingServiceImplement(HotelsRepository hotelsRepository,NotificationService notificationService,UserInfoRepository userInfoRepository,UserRepository userRepository,RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.notificationService = notificationService;
        this.hotelsRepository = hotelsRepository;
    }


    @Transactional
    @Override
    public void createRating(RatingDTO ratingDTO) {
        User user= userRepository.findByUserName(ThreadContext.getUserDetail().getUsername()).orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_ALREADY_EXIST));
        if (ratingRepository.existsByUserIdAndHotelId(user.getId(),ratingDTO.getHotelId())){
            throw new BusinessException(ErrorCode.USER_ALREADY_HAVE_RATING);
        }
        Rating rating=Rating.builder()
                .hotelId(ratingDTO.getHotelId())
                .userId(user.getId())
                .reason(ratingDTO.getReason())
                .rating(ratingDTO.getRating())
                .roomId(ratingDTO.getRoomId())
                .build();

        notificationService.createNotification("Bạn đã đánh giá khách sạn "+hotelsRepository.getHotelNameById(ratingDTO.getHotelId()));
        ratingRepository.save(rating);
    }

    @Override
    public List<RatingVO> getHotelRating(RatingGetDTO ratingGetDTO) {
        List<Rating> list=ratingRepository.getRatingByHotelIdAndRoomId(ratingGetDTO.getHotelId(), ratingGetDTO.getRoomId());
        List<RatingVO> voList=new ArrayList<>();
        list.forEach(c->{
            UserInfo userInfo=userInfoRepository.getUserInfoByUserId(c.getUserId()).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_ALREADY_EXIST));
            User user=userRepository.findById(userInfo.getUserId()).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_ALREADY_EXIST));
            RatingVO ratingVO=RatingVO.builder()
                    .avatar(userInfo.getAvatar())
                    .userName(user.getUsername())
                    .rating(c.getRating())
                    .reason(c.getReason())
                    .createdDate(c.getCreatedDate())
                    .build();
            voList.add(ratingVO);
        });
        return voList;
    }
}
