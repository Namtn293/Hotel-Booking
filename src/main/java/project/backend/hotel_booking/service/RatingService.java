package project.backend.hotel_booking.service;

import project.backend.hotel_booking.model.dto.RatingDTO;
import project.backend.hotel_booking.model.dto.RatingGetDTO;
import project.backend.hotel_booking.model.vo.RatingVO;

import java.util.List;

public interface RatingService {
    void createRating(RatingDTO ratingDTO);
    List<RatingVO> getHotelRating(RatingGetDTO ratingGetDTO);
}

