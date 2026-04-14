package project.backend.hotel_booking.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.model.dto.RatingDTO;
import project.backend.hotel_booking.model.dto.RatingGetDTO;
import project.backend.hotel_booking.model.vo.RatingVO;
import project.backend.hotel_booking.service.RatingService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public SuccessResponse<String> createRating(
            @RequestParam("rating") String ratingJson,
            @RequestParam(value = "file",required = false) MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RatingDTO ratingDTO = mapper.readValue(ratingJson, RatingDTO.class);
        ratingService.createRating(ratingDTO, file);
        return ResponseUtil.ok("Create Rating success");
    }

    @PostMapping("/get/all/{roomId}")
    public SuccessResponse<List<RatingVO>> getAllHotelRatings(@PathVariable Long roomId){
        return ResponseUtil.ok("Get all ratings success",ratingService.getHotelRating(roomId));
    }
}
