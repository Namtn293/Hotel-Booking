package project.backend.hotel_booking.service;

import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.entity.Room;
import project.backend.hotel_booking.model.dto.RoomCreateDTO;
import project.backend.hotel_booking.model.dto.RoomFindDTO;
import project.backend.hotel_booking.model.dto.RoomUpdateDTO;
import project.backend.hotel_booking.model.vo.RoomPVO;
import project.backend.hotel_booking.model.vo.RoomVO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    void createRoom(Long hotelId, RoomCreateDTO roomCreateDTO, MultipartFile image) throws IOException;
    RoomVO getRoomById(Long roomId);
//    List<RoomVO> getAllRoomsInHotel(Long id);
    void updateRoomById(Long id, RoomUpdateDTO roomUpdateDTO, MultipartFile image) throws IOException;
    void deleteRoomById(Long Id);
    List<RoomPVO> getAllRoomsInPartner();
    List<RoomVO> getAllRooms();
    List<RoomVO> findRoomByRequire(RoomFindDTO roomFindDTO);

    RoomVO convertToRoomVO(Room room);
    RoomPVO convertToRoomPVO(Room room);
}
