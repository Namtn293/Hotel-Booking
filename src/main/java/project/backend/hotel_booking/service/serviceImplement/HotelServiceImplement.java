package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.Hotel;
import project.backend.hotel_booking.entity.Image;
import project.backend.hotel_booking.entity.PartnerInfo;
import project.backend.hotel_booking.entity.Room;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.enumration.HotelEnum;
import project.backend.hotel_booking.model.dto.HotelRegisterDTO;
import project.backend.hotel_booking.model.dto.UpdateHotelDTO;
import project.backend.hotel_booking.model.vo.HotelInfoAVO;
import project.backend.hotel_booking.model.vo.HotelInfoPVO;
import project.backend.hotel_booking.model.vo.HotelInfoVO;
import project.backend.hotel_booking.repository.*;
import project.backend.hotel_booking.model.vo.HotelStatisticVO;
import project.backend.hotel_booking.repository.HotelsRepository;
import project.backend.hotel_booking.repository.OrderRoomRepository;
import project.backend.hotel_booking.repository.PartnerInfoRepository;
import project.backend.hotel_booking.service.HotelService;
import project.backend.hotel_booking.service.ImageService;
import project.backend.hotel_booking.service.NotificationService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImplement implements HotelService {
    private final HotelsRepository hotelsRepository;
    private final PartnerInfoRepository partnerInfoRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final OrderRoomRepository orderRoomRepository;
    private final RoomRepository roomRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepositoty;
    public HotelServiceImplement(HotelsRepository hotelsRepository, PartnerInfoRepository partnerInfoRepository, UserRepository userRepository, NotificationService notificationService, OrderRoomRepository orderRoomRepository, RoomRepository roomRepository, ImageService imageService, ImageRepository imageRepositoty) {
        this.hotelsRepository = hotelsRepository;
        this.partnerInfoRepository = partnerInfoRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.orderRoomRepository = orderRoomRepository;
        this.roomRepository = roomRepository;
        this.imageService = imageService;
        this.imageRepositoty = imageRepositoty;
    }

    @Override
    public List<HotelInfoAVO> getAllHotels() {
        List<HotelInfoAVO> hotelInfoAVOS = new ArrayList<>();
        List<Hotel> hotels = hotelsRepository.findAll();
        hotels.forEach((h)->{
            hotelInfoAVOS.add(convertToHotelInfoAVO(h));
        });
        return hotelInfoAVOS;
    }

    @Override
    public List<HotelInfoPVO> getAllMyHotels() {
        List<HotelInfoPVO> hotelInfoPVOS = new ArrayList<>();
        System.out.println(ThreadContext.getUserDetail().getUsername());

        Long userId = userRepository.findIdByUserName(ThreadContext.getUserDetail().getUsername())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        System.out.println(userId);
        PartnerInfo partnerInfo = partnerInfoRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.PARTNER_NOT_ALREADY_EXIST));

        List<Hotel> hotels = hotelsRepository.findAllByPartnerId(partnerInfo.getId());
        hotels.forEach((h)->{
            hotelInfoPVOS.add(convertToHotelInfoPVO(h));
        });
        return hotelInfoPVOS;
    }

    @Override
    public void updateHotelInfo(Long hotelId, UpdateHotelDTO updateHotelDTO, MultipartFile image) throws IOException {
        Hotel hotel = hotelsRepository.findById(hotelId)
                .orElseThrow(()->new BusinessException(ErrorCode.HOTEL_NOT_EXIST));
        if(updateHotelDTO.getHotelName()!=null)
            hotel.setHotelName(updateHotelDTO.getHotelName());
        if(updateHotelDTO.getDescription()!=null)
            hotel.setDescription(updateHotelDTO.getDescription());
        if(updateHotelDTO.getEmail()!=null)
            hotel.setEmail(updateHotelDTO.getEmail());
        if(updateHotelDTO.getPhoneNumber()!=null)
            hotel.setPhoneNumber(updateHotelDTO.getPhoneNumber());
        if(updateHotelDTO.getAddress()!=null)
            hotel.setAddress(updateHotelDTO.getAddress());
        if(image!=null && !image.isEmpty()){
            if(hotel.getImageId()!=null){
                Image oldImage = imageRepositoty.findById(hotel.getImageId())
                        .orElseThrow(()->new BusinessException(ErrorCode.IMAGE_NOT_EXIST));
                imageService.deleteImage(oldImage.getId());
            }
            Image newImage = imageService.uploadImage(image);
            hotel.setImageId(newImage.getId());
        }
        notificationService.createNotification("Cập nhật thành công khách sạn "+String.format("KS%02d",hotelId));
        hotelsRepository.save(hotel);
    }

    @Override
    public void changeStatus(Long hotelId,HotelEnum status) {
        Hotel hotel = hotelsRepository.findById(hotelId)
                .orElseThrow(()->new BusinessException(ErrorCode.HOTEL_NOT_EXIST));
        hotel.setHotelEnum(status);
        hotelsRepository.save(hotel);
    }

    @Override
    public void changeActiveStatus(Long hotelId, ActiveStatus activeStatus) {
        Hotel hotel = hotelsRepository.findById(hotelId)
                .orElseThrow(()->new BusinessException(ErrorCode.HOTEL_NOT_EXIST));
        hotel.setActiveStatus(activeStatus);
        hotelsRepository.save(hotel);
    }

    @Transactional
    @Override
    public void deleteHotelInfo(Long hotelId) {
        Hotel hotel = hotelsRepository.findById(hotelId)
                .orElseThrow(()->new BusinessException(ErrorCode.HOTEL_NOT_EXIST));
        orderRoomRepository.deleteAllByHotelId(hotelId);
        roomRepository.deleteAllByHotelId(hotelId);
        if(hotel.getImageId()!=null) imageService.deleteImage(hotel.getImageId());
        hotelsRepository.deleteById(hotelId);
    }

    @Transactional
    @Override
    public HotelInfoVO registerHotel(HotelRegisterDTO hotelRegisterDTO,MultipartFile image) throws IOException {
        Long userId = userRepository.findIdByUserName(ThreadContext.getUserDetail().getUsername())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        System.out.println(userId);
        PartnerInfo partnerInfo = partnerInfoRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.PARTNER_NOT_ALREADY_EXIST));

        if (!StringUtils.hasText(hotelRegisterDTO.getHotelName()) ||
                !StringUtils.hasText(hotelRegisterDTO.getAddress())) {
            throw new BusinessException(ErrorCode.NULL_FIELD);
        }
        Image newImage = null;
        if (image != null && !image.isEmpty()) {
            newImage = imageService.uploadImage(image);
        }
        System.out.println(newImage.getUrl());
        Hotel hotel = Hotel.builder()
                .partnerId(partnerInfo.getId())
                .hotelName(hotelRegisterDTO.getHotelName())
                .address(hotelRegisterDTO.getAddress())
                .description(hotelRegisterDTO.getDescription())
                .activeStatus(ActiveStatus.PENDING)
                .hotelEnum(HotelEnum.PENDING)
                .imageId(newImage != null ? newImage.getId() : null)
                .imageId(newImage.getId())
                .build();
        hotelsRepository.save(hotel);
        notificationService.createNotification("Cập nhật thành công khách sạn "+String.format("KS%02d",hotel.getId()));
        return convertToHotelInfoVO(hotel);
    }

    @Override
    public HotelInfoVO convertToHotelInfoVO(Hotel hotel) {
        HotelInfoVO hotelInfoVO = new HotelInfoVO();
        hotelInfoVO.setHotelId(hotel.getId());
        hotelInfoVO.setHotelName(hotel.getHotelName());
        hotelInfoVO.setPartnerName(partnerInfoRepository.findPartnerNameById(hotel.getPartnerId())
                .orElseThrow(()->new BusinessException(ErrorCode.PARTNER_NOT_ALREADY_EXIST)));
        hotelInfoVO.setStatus(hotel.getHotelEnum());
        return hotelInfoVO;
    }

    @Override
    public HotelInfoAVO convertToHotelInfoAVO(Hotel hotel) {
        HotelInfoAVO hotelInfoAVO = new HotelInfoAVO();
        hotelInfoAVO.setId(hotel.getId());
        hotelInfoAVO.setHotelName(hotel.getHotelName());
        hotelInfoAVO.setPartnerName(partnerInfoRepository.findPartnerNameById(hotel.getPartnerId())
                .orElseThrow(()->new BusinessException(ErrorCode.PARTNER_NOT_ALREADY_EXIST)));
        hotelInfoAVO.setEmail(hotel.getEmail());
        hotelInfoAVO.setCreatedDate(hotel.getCreatedDate());
        hotelInfoAVO.setStatus(hotel.getHotelEnum());

        return hotelInfoAVO;
    }

    @Override
    public HotelInfoPVO convertToHotelInfoPVO(Hotel hotel) {
        HotelInfoPVO hotelInfoPVO = new HotelInfoPVO();
        hotelInfoPVO.setHotelId(hotel.getId());
        hotelInfoPVO.setHotelName(hotel.getHotelName());
        hotelInfoPVO.setDescription(hotel.getDescription());
        hotelInfoPVO.setAddress(hotel.getAddress());
        System.out.println(hotel.getId());
        if (hotel.getImageId() != null) {
            hotelInfoPVO.setUrl(imageRepositoty.findById(hotel.getImageId())
                    .map(Image::getUrl)
                    .orElse(null));
        }
        return hotelInfoPVO;
    }

    public List<HotelStatisticVO> getTop5GHotelStatistic() {
        List<Hotel> list=hotelsRepository.getTop5Hotel();
        List<HotelStatisticVO> hotelStatisticVOS=new ArrayList<>();
        list.forEach(c->{
            hotelStatisticVOS.add(HotelStatisticVO.builder()
                            .createdDate(c.getCreatedDate())
                            .hotelName(c.getHotelName())
                    .build());
        });
        return hotelStatisticVOS;
    }

    @Override
    public Long getNewHotelPending() {
        return hotelsRepository.getNewHotelPending(LocalDate.now());
    }
}
