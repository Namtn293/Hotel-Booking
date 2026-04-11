package project.backend.hotel_booking.service;

import project.backend.hotel_booking.entity.Hotel;
import project.backend.hotel_booking.model.dto.HotelRegisterDTO;
import project.backend.hotel_booking.model.dto.UpdateHotelDTO;
import project.backend.hotel_booking.model.vo.HotelInfoAVO;
import project.backend.hotel_booking.model.vo.HotelInfoVO;

import java.util.List;

public interface HotelService {
    List<HotelInfoAVO> getAllHotels();
    List<HotelInfoVO> getAllMyHotels();
    void updateHotelInfo(Long hotelId,UpdateHotelDTO updateHotelDTO);
    HotelInfoVO registerHotel(HotelRegisterDTO hotelRegisterDTO);
    HotelInfoVO convertToHotelInfoVO(Hotel hotel);
    HotelInfoAVO convertToHotelInfoAVO(Hotel hotel);
}
