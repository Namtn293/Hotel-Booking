package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import project.backend.hotel_booking.model.vo.HotelInfoVO;
import project.backend.hotel_booking.service.HotelService;

import java.util.List;

@Service
public class HotelServiceImplement implements HotelService {
    @Override
    public List<HotelInfoVO> getAllHotels() {

        return List.of();
    }
}
