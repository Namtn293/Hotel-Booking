package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.Hotel;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.model.dto.HotelRegisterDTO;
import project.backend.hotel_booking.model.dto.UpdateHotelDTO;
import project.backend.hotel_booking.model.vo.HotelInfoVO;
import project.backend.hotel_booking.repository.HotelsRepository;
import project.backend.hotel_booking.service.HotelService;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImplement implements HotelService {
    private final HotelsRepository hotelsRepository;

    public HotelServiceImplement(HotelsRepository hotelsRepository) {
        this.hotelsRepository = hotelsRepository;
    }

    @Override
    public List<HotelInfoVO> getAllHotels() {
        List<HotelInfoVO> hotelInfoVOS = new ArrayList<>();
        List<Hotel> hotels = hotelsRepository.findAll();
        hotels.forEach((h)->{
            hotelInfoVOS.add(convertToHotelInfoVO(h));
        });
        return hotelInfoVOS;
    }

    @Override
    public List<HotelInfoVO> getAllMyHotels(Long partnerId) {
        List<HotelInfoVO> hotelInfoVOS = new ArrayList<>();
        List<Hotel> hotels = hotelsRepository.findAllByPartnerId(partnerId);
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
        if(updateHotelDTO.getActiveStatus()!=null)
            hotel.setActiveStatus(updateHotelDTO.getActiveStatus());
        if(updateHotelDTO.getAddress()!=null)
            hotel.setAddress(updateHotelDTO.getAddress());
        hotelsRepository.save(hotel);
    }

    @Override
    public HotelInfoVO registerHotel(Long partnerId,HotelRegisterDTO hotelRegisterDTO) {
        if(hotelRegisterDTO.getHotelName().isEmpty() ||
        hotelRegisterDTO.getHotelName().isEmpty())
            throw new BusinessException(ErrorCode.NULL_FIELD);
        Hotel hotel = Hotel.builder()
                .partnerId(partnerId)
                .hotelName(hotelRegisterDTO.getHotelName())
                .address(hotelRegisterDTO.getAddress())
                .description(hotelRegisterDTO.getDescription())
                .activeStatus(ActiveStatus.PENDING)
                .build();
        hotelsRepository.save(hotel);
        return convertToHotelInfoVO(hotel);
    }

    @Override
    public HotelInfoVO convertToHotelInfoVO(Hotel hotel) {
        HotelInfoVO hotelInfoVO = new HotelInfoVO();
        hotelInfoVO.setHotelName(hotel.getHotelName());
        hotelInfoVO.setStatus(hotel.getActiveStatus());
        return hotelInfoVO;
    }
}
