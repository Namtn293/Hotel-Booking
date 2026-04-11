package project.backend.hotel_booking.service;

import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.entity.Hotel;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.HotelEnum;
import project.backend.hotel_booking.model.dto.HotelRegisterDTO;
import project.backend.hotel_booking.model.dto.UpdateHotelDTO;
import project.backend.hotel_booking.model.vo.HotelInfoAVO;
import project.backend.hotel_booking.model.vo.HotelInfoPVO;
import project.backend.hotel_booking.model.vo.HotelInfoVO;

import java.io.IOException;
import java.util.List;

public interface HotelService {
    List<HotelInfoAVO> getAllHotels();
    List<HotelInfoPVO> getAllMyHotels();
    void updateHotelInfo(Long hotelId,UpdateHotelDTO updateHotelDTO,MultipartFile image) throws IOException;
    void changeStatus(Long hotelId,HotelEnum status);
    void changeActiveStatus(Long hotelId, ActiveStatus activeStatus);
    void deleteHotelInfo(Long hotelId);
    HotelInfoVO registerHotel(HotelRegisterDTO hotelRegisterDTO, MultipartFile image) throws IOException;
    HotelInfoVO convertToHotelInfoVO(Hotel hotel);
    HotelInfoAVO convertToHotelInfoAVO(Hotel hotel);
    HotelInfoPVO convertToHotelInfoPVO(Hotel hotel);
}
