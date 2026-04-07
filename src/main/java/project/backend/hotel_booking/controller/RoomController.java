package project.backend.hotel_booking.controller;

import org.springframework.web.bind.annotation.*;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.entity.Room;
import project.backend.hotel_booking.model.dto.RoomCreateDTO;
import project.backend.hotel_booking.model.dto.RoomUpdateDTO;
import project.backend.hotel_booking.model.vo.RoomVO;
import project.backend.hotel_booking.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("api/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create/{hotelId}")
    SuccessResponse<String> createRoom(@PathVariable Long hotelId,@RequestBody RoomCreateDTO roomCreateDTO){
        roomService.createRoom(hotelId,roomCreateDTO);
        return ResponseUtil.ok(
                "create room success"
        );
    }

    @PostMapping("/get/{roomId}")
    SuccessResponse<RoomVO> getRoomById(@PathVariable Long roomId){
        return ResponseUtil.ok(
                "get room success",
                roomService.getRoomById(roomId)
        );
    }

    @PostMapping("/get/rooms/{hotelId}")
    SuccessResponse<List<RoomVO>> getAllRoomsInHotel(@PathVariable Long hotelId){
        return ResponseUtil.ok(
                "get rooms in hotel success",
                roomService.getAllRoomsInHotel(hotelId)
        );
    }

    @PostMapping("/update/{roomId}")
    SuccessResponse<String> updateRoomById(@PathVariable Long roomId, @RequestBody RoomUpdateDTO roomUpdateDTO){
        roomService.updateRoomById(roomId,roomUpdateDTO);
        return ResponseUtil.ok(
                "update room success"
        );
    }

    @PostMapping("/delete/{roomId}")
    SuccessResponse<RoomVO> deleteRoomById(@PathVariable Long roomId){
        roomService.deleteRoomById(roomId);
        return ResponseUtil.ok(
                "delete room success"
        );
    }

    @GetMapping("/all")
    SuccessResponse<List<RoomVO>> getAllRooms(){
        return ResponseUtil.ok(
                "Get all rooms success",
                roomService.getAllRooms()
        );
    }
}
