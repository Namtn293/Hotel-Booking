package project.backend.hotel_booking.controller;

import org.springframework.web.bind.annotation.*;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.model.dto.HotelRegisterDTO;
import project.backend.hotel_booking.model.dto.UpdateHotelDTO;
import project.backend.hotel_booking.model.vo.HotelInfoVO;
import project.backend.hotel_booking.service.HotelService;

import java.util.List;

@RestController
@RequestMapping("api/hotel")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/register/{partnerId}")
    SuccessResponse<HotelInfoVO> registerHotel(@PathVariable Long partnerId, @RequestBody HotelRegisterDTO hotelRegisterDTO){
           return ResponseUtil.ok(
                   "register hotel success",
                   hotelService.registerHotel(partnerId,hotelRegisterDTO)
           );
    }

    @PostMapping("/admin/get")
    SuccessResponse<List<HotelInfoVO>> getAllHotels(){
            return ResponseUtil.ok(
                    "get all hotels success",
                    hotelService.getAllHotels()
            );
    }

    @PostMapping("/partner/get/{partnerId}")
    SuccessResponse<List<HotelInfoVO>> getAllMyHotels(@PathVariable Long partnerId){
        return ResponseUtil.ok(
                "get all hotels success",
                hotelService.getAllMyHotels(partnerId)
        );
    }

    @PostMapping("/partner/update/{hotelId}")
    SuccessResponse<String> updateHotelInfo(@PathVariable Long hotelId, @RequestBody UpdateHotelDTO updateHotelDTO){
        hotelService.updateHotelInfo(hotelId,updateHotelDTO);
        return ResponseUtil.ok(
                "update hotel info success"
        );
    }
}
