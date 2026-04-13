package project.backend.hotel_booking.service;

import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.model.dto.RatingDTO;
import project.backend.hotel_booking.model.dto.RatingGetDTO;
import project.backend.hotel_booking.model.vo.RatingVO;

import java.io.IOException;
import java.util.List;

public interface RatingService {
    void createRating(RatingDTO ratingDTO, MultipartFile file) throws IOException;
    List<RatingVO> getHotelRating(Long roomId);
}

