package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.Room;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.model.dto.RoomCreateDTO;
import project.backend.hotel_booking.model.dto.RoomUpdateDTO;
import project.backend.hotel_booking.model.vo.RoomVO;
import project.backend.hotel_booking.repository.HotelsRepository;
import project.backend.hotel_booking.repository.RoomRepository;
import project.backend.hotel_booking.service.NotificationService;
import project.backend.hotel_booking.service.RoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImplement implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelsRepository hotelsRepository;
    private final NotificationService notificationService;

    public RoomServiceImplement(NotificationService notificationService,RoomRepository roomRepository, HotelsRepository hotelsRepository) {
        this.roomRepository = roomRepository;
        this.hotelsRepository = hotelsRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void createRoom(Long hotelId, RoomCreateDTO roomCreateDTO) {
        Room room = Room.builder()
                .roomName(roomCreateDTO.getRoomName())
                .capacity(roomCreateDTO.getCapacity())
                .price(roomCreateDTO.getPrice())
                .qualityEnum(roomCreateDTO.getQualityEnum())
                .activeStatus(ActiveStatus.PENDING)
                .hotelId(hotelId)
                .build();
        notificationService.createNotification("Bạn đã tạo thành công phòng mới "+String.format("ROOM%2d"+room.getId()));
        roomRepository.save(room);
    }

    @Override
    public RoomVO getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()->new BusinessException(ErrorCode.ROOM_NOT_EXIST));
        return convertToRoomVO(room);
    }

    @Override
    public List<RoomVO> getAllRoomsInHotel(Long hotelId) {
        List<Room> rooms = roomRepository.findAllByHotelId(hotelId);
        List<RoomVO> roomVOS = new ArrayList<>();
        rooms.forEach((r)->{
            roomVOS.add(convertToRoomVO(r));
        });
        return roomVOS;
    }

    @Override
    public void updateRoomById(Long id, RoomUpdateDTO roomUpdateDTO) {
        Room room = roomRepository.findById(id)
                .orElseThrow(()-> new BusinessException(ErrorCode.ROOM_NOT_EXIST));
        if(!roomUpdateDTO.getRoomName().isEmpty())
            room.setRoomName(roomUpdateDTO.getRoomName());
        if(roomUpdateDTO.getCapacity()!=null)
            room.setCapacity(roomUpdateDTO.getCapacity());
        if(roomUpdateDTO.getActiveStatus()!=null)
            room.setActiveStatus(roomUpdateDTO.getActiveStatus());
        System.out.println(roomUpdateDTO.getPrice());
        if(roomUpdateDTO.getPrice()>0)
            room.setPrice(roomUpdateDTO.getPrice());
        if(roomUpdateDTO.getQualityEnum()!=null)
            room.setQualityEnum(roomUpdateDTO.getQualityEnum());
        notificationService.createNotification("Bạn đã cập nhật thành công phòng "+String.format("ROOM%2d"+room.getId()));
        roomRepository.save(room);
    }

    @Override
    public void deleteRoomById(Long id) {
        notificationService.createNotification("Bạn đã xóa thành công phòng "+String.format("ROOM%2d"+id));
        roomRepository.deleteById(id);
    }

    @Override
    public List<RoomVO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomVO> roomVOS = new ArrayList<>();
        rooms.forEach((r)->{
            roomVOS.add(convertToRoomVO(r));
        });
        return roomVOS;
    }

    @Override
    public RoomVO convertToRoomVO(Room room) {
        RoomVO roomVO = new RoomVO();
        roomVO.setRoomName(room.getRoomName());
        roomVO.setPrice(room.getPrice());
        roomVO.setCapacity(room.getCapacity());
        roomVO.setActiveStatus(room.getActiveStatus());
        roomVO.setQualityEnum(room.getQualityEnum());
        roomVO.setHotelName(hotelsRepository.getHotelNameById(room.getHotelId()));
        return roomVO;
    }
}
