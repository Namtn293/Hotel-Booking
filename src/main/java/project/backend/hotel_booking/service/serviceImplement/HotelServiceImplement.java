package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.Hotel;
import project.backend.hotel_booking.entity.PartnerInfo;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.enumration.HotelEnum;
import project.backend.hotel_booking.model.dto.HotelRegisterDTO;
import project.backend.hotel_booking.model.dto.UpdateHotelDTO;
import project.backend.hotel_booking.model.vo.HotelInfoAVO;
import project.backend.hotel_booking.model.vo.HotelInfoVO;
import project.backend.hotel_booking.repository.HotelsRepository;
import project.backend.hotel_booking.repository.OrderRoomRepository;
import project.backend.hotel_booking.repository.PartnerInfoRepository;
import project.backend.hotel_booking.service.HotelService;
import project.backend.hotel_booking.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImplement implements HotelService {
    private final HotelsRepository hotelsRepository;
    private final PartnerInfoRepository partnerInfoRepository;
    private final UserRepository userRepository;
    public HotelServiceImplement(HotelsRepository hotelsRepository, PartnerInfoRepository partnerInfoRepository, UserRepository userRepository) {
        this.hotelsRepository = hotelsRepository;
        this.partnerInfoRepository = partnerInfoRepository;
        this.userRepository = userRepository;
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
    public List<HotelInfoVO> getAllMyHotels() {
        List<HotelInfoVO> hotelInfoVOS = new ArrayList<>();
        System.out.println(ThreadContext.getUserDetail().getUsername());

        Long userId = userRepository.findIdByUserName(ThreadContext.getUserDetail().getUsername())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        System.out.println(userId);
        PartnerInfo partnerInfo = partnerInfoRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.PARTNER_NOT_ALREADY_EXIST));

        List<Hotel> hotels = hotelsRepository.findAllByPartnerId(partnerInfo.getId());
        hotels.forEach((h)->{
            hotelInfoVOS.add(convertToHotelInfoVO(h));
        });
        return hotelInfoVOS;
    }

    @Override
    public void updateHotelInfo(Long hotelId, UpdateHotelDTO updateHotelDTO) {
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
        notificationService.createNotification("Cập nhật thành công khách sạn "+String.format("KS%02d",hotelId));
        hotelsRepository.save(hotel);
    }

    @Override
    public HotelInfoVO registerHotel(HotelRegisterDTO hotelRegisterDTO) {
        Long userId = userRepository.findIdByUserName(ThreadContext.getUserDetail().getUsername())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        PartnerInfo partnerInfo = partnerInfoRepository.findById(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.PARTNER_NOT_ALREADY_EXIST));

        if(hotelRegisterDTO.getHotelName().isEmpty() ||
        hotelRegisterDTO.getAddress().isEmpty())
            throw new BusinessException(ErrorCode.NULL_FIELD);
        Hotel hotel = Hotel.builder()
                .partnerId(partnerInfo.getId())
                .hotelName(hotelRegisterDTO.getHotelName())
                .address(hotelRegisterDTO.getAddress())
                .description(hotelRegisterDTO.getDescription())
                .hotelEnum(HotelEnum.PENDING)
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
}
