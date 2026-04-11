package project.backend.hotel_booking.controller;

import org.springframework.web.bind.annotation.*;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.model.dto.RatingDTO;
import project.backend.hotel_booking.model.dto.RatingGetDTO;
import project.backend.hotel_booking.model.vo.RatingVO;
import project.backend.hotel_booking.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/create")
    public SuccessResponse<String> createRating(@RequestBody RatingDTO ratingDTO){
        ratingService.createRating(ratingDTO);
        return ResponseUtil.ok("Create Rating success");
    }

    @PostMapping("/get/all")
    public SuccessResponse<List<RatingVO>> getAllHotelRatings(@RequestBody RatingGetDTO ratingDTO){
        return ResponseUtil.ok("Get all ratings success",ratingService.getHotelRating(ratingDTO));
    }
}
