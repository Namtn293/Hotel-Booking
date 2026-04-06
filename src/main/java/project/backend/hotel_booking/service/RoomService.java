package project.backend.hotel_booking.service;

import project.backend.hotel_booking.entity.Room;
import project.backend.hotel_booking.model.dto.RoomCreateDTO;
import project.backend.hotel_booking.model.dto.RoomUpdateDTO;
import project.backend.hotel_booking.model.vo.RoomVO;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    void createRoom(Long hotelId, RoomCreateDTO roomCreateDTO);
    RoomVO getRoomById(Long roomId);
    List<RoomVO> getAllRoomsInHotel(Long id);
    void updateRoomById(Long id, RoomUpdateDTO roomUpdateDTO);
    void deleteRoomById(Long Id);

    RoomVO convertToRoomVO(Room room);
}
