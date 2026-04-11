package project.backend.hotel_booking.controller;

import org.springframework.web.bind.annotation.*;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.model.dto.HotelRegisterDTO;
import project.backend.hotel_booking.model.dto.UpdateHotelDTO;
import project.backend.hotel_booking.model.vo.HotelInfoAVO;
import project.backend.hotel_booking.model.vo.HotelInfoVO;
import project.backend.hotel_booking.model.vo.HotelStatisticVO;
import project.backend.hotel_booking.service.HotelService;

import java.util.List;

import static project.backend.hotel_booking.core.util.ResponseUtil.ok;

@RestController
@RequestMapping("api/hotel")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/register/{partnerId}")
    SuccessResponse<HotelInfoVO> registerHotel(@RequestBody HotelRegisterDTO hotelRegisterDTO){
           return ok(
                   "register hotel success",
                   hotelService.registerHotel(hotelRegisterDTO)
           );
    }

    @GetMapping("/admin")
    SuccessResponse<List<HotelInfoAVO>> getAllHotels(){
            return ok(
                    "get all hotels success",
                    hotelService.getAllHotels()
            );
    }

    @GetMapping("/partner")
    SuccessResponse<List<HotelInfoVO>> getAllMyHotels(){
        return ok(
                "get all hotels success",
                hotelService.getAllMyHotels()
        );
    }

    @PutMapping("/partner/update/{hotelId}")
    SuccessResponse<String> updateHotelInfo(@PathVariable Long hotelId, @RequestBody UpdateHotelDTO updateHotelDTO){
        hotelService.updateHotelInfo(hotelId,updateHotelDTO);
        return ok(
                "update hotel info success"
        );
    }


    @GetMapping("/get-top5-hotel-pending")
    SuccessResponse<List<HotelStatisticVO>> getTop5HotelPending(){
        return ok("Get success",hotelService.getTop5GHotelStatistic());
    }

    @GetMapping("/hotel-pending-total")
    public SuccessResponse<Long> hotelPendingTotal(){
        return ResponseUtil.ok("Get success",hotelService.getNewHotelPending());
    }

}
