package project.backend.hotel_booking.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.HotelEnum;
import project.backend.hotel_booking.model.dto.HotelRegisterDTO;
import project.backend.hotel_booking.model.dto.UpdateHotelDTO;
import project.backend.hotel_booking.model.vo.HotelInfoAVO;
import project.backend.hotel_booking.model.vo.HotelInfoPVO;
import project.backend.hotel_booking.model.vo.HotelInfoVO;
import project.backend.hotel_booking.model.vo.HotelStatisticVO;
import project.backend.hotel_booking.service.HotelService;

import java.io.IOException;
import java.util.List;

import static project.backend.hotel_booking.core.util.ResponseUtil.ok;

@RestController
@RequestMapping("api/hotel")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/register")
    SuccessResponse<HotelInfoVO> registerHotel(@RequestPart("dto") HotelRegisterDTO hotelRegisterDTO, @RequestPart(value = "image",required = false)MultipartFile image) throws IOException {
           return ResponseUtil.ok(
                   "register hotel success",
                   hotelService.registerHotel(hotelRegisterDTO,image)
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
    SuccessResponse<List<HotelInfoPVO>> getAllMyHotels(){
        return ResponseUtil.ok(
                "get all my hotels success",
                hotelService.getAllMyHotels()
        );
    }

    @PutMapping("/update/{hotelId}")
    SuccessResponse<String> updateHotelInfo(@PathVariable Long hotelId, @RequestPart("dto") UpdateHotelDTO updateHotelDTO,@RequestPart(value = "image",required = false) MultipartFile image) throws IOException {
        hotelService.updateHotelInfo(hotelId,updateHotelDTO,image);
        return ResponseUtil.ok(
                "update hotel info success"
        );
    }

    @PutMapping("/status/{hotelId}/{status}")
    SuccessResponse<String> changeStatus(@PathVariable Long hotelId, @PathVariable HotelEnum status){
        hotelService.changeStatus(hotelId,status);
        return ResponseUtil.ok(
                "change status success"
        );
    }

    @PutMapping("/active-status/{hotelId}/{status}")
    SuccessResponse<String> changeActiveStatus(@PathVariable Long hotelId, @PathVariable ActiveStatus status){
        hotelService.changeActiveStatus(hotelId,status);
        return ResponseUtil.ok(
                "change active status success"
        );
    }

    @DeleteMapping("/{hotelId}")
    SuccessResponse<String> deleteHotel(@PathVariable Long hotelId) {
        hotelService.deleteHotelInfo(hotelId);
        return ResponseUtil.ok(
                "delete hotel info success"
        );
    }

    @GetMapping("/get-top5-hotel-pending")
    SuccessResponse<List<HotelStatisticVO>> getTop5HotelPending(){
        return ResponseUtil.ok("Get success",hotelService.getTop5GHotelStatistic());
    }

    @GetMapping("/hotel-pending-total")
    public SuccessResponse<Long> hotelPendingTotal(){
        return ResponseUtil.ok("Get success",hotelService.getNewHotelPending());
    }
}
