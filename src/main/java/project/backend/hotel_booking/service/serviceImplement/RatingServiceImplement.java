package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.core.auth.entity.User;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.Image;
import project.backend.hotel_booking.entity.Rating;
import project.backend.hotel_booking.entity.UserInfo;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.model.dto.RatingDTO;
import project.backend.hotel_booking.model.dto.RatingGetDTO;
import project.backend.hotel_booking.model.vo.RatingVO;
import project.backend.hotel_booking.repository.*;
import project.backend.hotel_booking.service.ImageService;
import project.backend.hotel_booking.service.NotificationService;
import project.backend.hotel_booking.service.RatingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingServiceImplement implements RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final NotificationService notificationService;
    private final HotelsRepository hotelsRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final RoomRepository roomRepository;

    public RatingServiceImplement(RoomRepository roomRepository,ImageRepository imageRepository, ImageService imageService, HotelsRepository hotelsRepository, NotificationService notificationService, UserInfoRepository userInfoRepository, UserRepository userRepository, RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.notificationService = notificationService;
        this.hotelsRepository = hotelsRepository;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.roomRepository = roomRepository;
    }


    @Transactional
    @Override
    public void createRating(RatingDTO ratingDTO, MultipartFile file) throws IOException {
        User user= userRepository.findByUserName(ThreadContext.getUserDetail().getUsername()).orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_ALREADY_EXIST));
        if (ratingRepository.existsByUserIdAndRoomId(user.getId(),ratingDTO.getRoomId())){
            throw new BusinessException(ErrorCode.USER_ALREADY_HAVE_RATING);
        }
        Image image=new Image();
        if (!file.isEmpty()) image=imageService.uploadImage(file);
        Rating rating=Rating.builder()
                .userId(user.getId())
                .reason(ratingDTO.getReason())
                .rating(ratingDTO.getRating())
                .imagineId(image.getId())
                .roomId(ratingDTO.getRoomId())
                .build();

        notificationService.createNotification("Bạn đã đánh giá phòng"+roomRepository.getRoomNameById(ratingDTO.getRoomId()));
        ratingRepository.save(rating);
    }

    @Override
    public List<RatingVO> getHotelRating(Long roomId) {
        List<Rating> list=ratingRepository.getRatingByRoomId(roomId);
        List<RatingVO> voList=new ArrayList<>();
        list.forEach(c->{
            UserInfo userInfo=userInfoRepository.getUserInfoByUserId(c.getUserId()).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_ALREADY_EXIST));
            User user=userRepository.findById(userInfo.getUserId()).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_ALREADY_EXIST));
            RatingVO ratingVO=RatingVO.builder()
                    .avatar(userInfo.getAvatar())
                    .userName(user.getUsername())
                    .rating(c.getRating())
                    .reason(c.getReason())
                    .url(imageRepository.getUrlById(c.getImagineId()))
                    .createdDate(c.getCreatedDate())
                    .build();
            voList.add(ratingVO);
        });
        return voList;
    }
}
