package project.backend.hotel_booking.service;

import project.backend.hotel_booking.model.vo.HotelInfoVO;

import java.util.List;

public interface HotelService {
    List<HotelInfoVO> getAllHotels();
}
