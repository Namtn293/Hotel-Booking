package project.backend.hotel_booking.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.entity.Room;
import project.backend.hotel_booking.model.dto.RoomCreateDTO;
import project.backend.hotel_booking.model.dto.RoomFindDTO;
import project.backend.hotel_booking.model.dto.RoomUpdateDTO;
import project.backend.hotel_booking.model.vo.RoomPVO;
import project.backend.hotel_booking.model.vo.RoomVO;
import project.backend.hotel_booking.service.RoomService;

import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDate;

@RestController
@RequestMapping("api/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create/{hotelId}")
    SuccessResponse<String> createRoom(@PathVariable Long hotelId, @RequestPart(value = "dto",required = false) RoomCreateDTO roomCreateDTO, @RequestPart(value = "image",required = false)MultipartFile image) throws IOException {
        roomService.createRoom(hotelId,roomCreateDTO,image);
        return ResponseUtil.ok(
                "create room success"
        );
    }

    @GetMapping("/{roomId}")
    SuccessResponse<RoomVO> getRoomById(@PathVariable Long roomId){
        return ResponseUtil.ok(
                "get room success",
                roomService.getRoomById(roomId)
        );
    }

//    @GetMapping("/{hotelId}")
//    SuccessResponse<List<RoomVO>> getAllRoomsInHotel(@PathVariable Long hotelId){
//        return ResponseUtil.ok(
//                "get rooms in hotel success",
//                roomService.getAllRoomsInHotel(hotelId)
//        );
//    }

    @PutMapping("/{roomId}")
    SuccessResponse<String> updateRoomById(@PathVariable Long roomId, @RequestPart(value = "dto",required = false) RoomUpdateDTO roomUpdateDTO,@RequestPart(value = "image",required = false)MultipartFile image) throws IOException {
        roomService.updateRoomById(roomId,roomUpdateDTO,image);
        return ResponseUtil.ok(
                "update room success"
        );
    }

    @DeleteMapping("/{roomId}")
    SuccessResponse<RoomVO> deleteRoomById(@PathVariable Long roomId){
        roomService.deleteRoomById(roomId);
        return ResponseUtil.ok(
                "delete room success"
        );
    }

    @GetMapping("/partner")
    SuccessResponse<List<RoomPVO>> getAllRoomsInPartner(){
        return ResponseUtil.ok(
                "Get all rooms success",
                roomService.getAllRoomsInPartner()
        );
    }

    @GetMapping("/find")
    SuccessResponse<List<RoomVO>> findRoomByRequire(
            @RequestParam String content,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam Long capacity
    ){
        RoomFindDTO roomFindDTO = new RoomFindDTO();
        roomFindDTO.setContent(content);
        roomFindDTO.setCheckIn(checkIn);
        roomFindDTO.setCheckOut(checkOut);
        roomFindDTO.setCapacity(capacity);

        return ResponseUtil.ok(
                "Get all room by require success",
                roomService.findRoomByRequire(roomFindDTO)
        );
    }
}
