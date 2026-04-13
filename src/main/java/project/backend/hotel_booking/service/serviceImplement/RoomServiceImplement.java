package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.core.auth.entity.User;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.*;
import project.backend.hotel_booking.enumration.ActiveStatus;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.enumration.PaymentStatus;
import project.backend.hotel_booking.model.dto.RoomCreateDTO;
import project.backend.hotel_booking.model.dto.RoomFindDTO;
import project.backend.hotel_booking.model.dto.RoomUpdateDTO;
import project.backend.hotel_booking.model.vo.RoomPVO;
import project.backend.hotel_booking.model.vo.RoomVO;
import project.backend.hotel_booking.repository.*;
import project.backend.hotel_booking.service.ImageService;
import project.backend.hotel_booking.service.NotificationService;
import project.backend.hotel_booking.service.RoomService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImplement implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelsRepository hotelsRepository;
    private final NotificationService notificationService;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PartnerInfoRepository partnerInfoRepository;
    private final OrderRoomRepository orderRoomRepository;

    public RoomServiceImplement(NotificationService notificationService, RoomRepository roomRepository, HotelsRepository hotelsRepository, ImageService imageService, ImageRepository imageRepository, UserRepository userRepository, PartnerInfoRepository partnerInfoRepository, OrderRoomRepository orderRoomRepository) {
        this.roomRepository = roomRepository;
        this.hotelsRepository = hotelsRepository;
        this.notificationService = notificationService;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.partnerInfoRepository = partnerInfoRepository;
        this.orderRoomRepository = orderRoomRepository;
    }
    @Transactional
    @Override
    public void createRoom(Long hotelId, RoomCreateDTO roomCreateDTO, MultipartFile image) throws IOException {
        Room room = Room.builder()
                .roomName(roomCreateDTO.getRoomName())
                .capacity(roomCreateDTO.getCapacity())
                .price(roomCreateDTO.getPrice())
                .qualityEnum(roomCreateDTO.getQualityEnum())
                .activeStatus(ActiveStatus.PENDING)
                .hotelId(hotelId)
                .description(roomCreateDTO.getDescription())
                .build();
        if(image!=null && !image.isEmpty()){
            Image newImage = imageService.uploadImage(image);
            room.setImageId(newImage.getId());
        }
        roomRepository.save(room);
        System.out.println(room.getId());
        notificationService.createNotification("Bạn đã tạo thành công phòng mới "+String.format("ROOM%2d",room.getId()));
    }

    @Override
    public RoomVO getRoomById(Long roomId) {
        System.out.println("ok");
        return roomRepository.getRoomByRoomId(roomId)
                .orElseThrow(()->new BusinessException(ErrorCode.ROOM_NOT_EXIST));
    }

//    @Override
//    public List<RoomVO> getAllRoomsInHotel(Long hotelId) {
//        List<Room> rooms = roomRepository.findAllByHotelId(hotelId);
//        List<RoomVO> roomVOS = new ArrayList<>();
//        rooms.forEach((r)->{
//            roomVOS.add(convertToRoomVO(r));
//        });
//        return roomVOS;
//    }

    @Transactional
    @Override
    public void updateRoomById(Long id, RoomUpdateDTO roomUpdateDTO, MultipartFile image) throws IOException {
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

        if(image!=null && !image.isEmpty()){
            if(room.getImageId()!=null){
                Image oldImage = imageRepository.findById(room.getImageId())
                        .orElseThrow(()->new BusinessException(ErrorCode.IMAGE_NOT_EXIST));
                imageService.deleteImage(oldImage.getId());
            }
            Image newImage = imageService.uploadImage(image);
            room.setImageId(newImage.getId());
        }

        notificationService.createNotification("Bạn đã cập nhật thành công phòng "+String.format("ROOM%2d",room.getId()));
        roomRepository.save(room);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(()->new BusinessException(ErrorCode.ROOM_NOT_EXIST));
        if(room.getImageId()!=null) imageService.deleteImage(room.getImageId());
        notificationService.createNotification("Bạn đã xóa thành công phòng "+String.format("ROOM%2d",id));
        roomRepository.deleteById(id);
    }

    @Override
    public List<RoomPVO> getAllRoomsInPartner() {
        User user = userRepository.findByUserName(ThreadContext.getUserDetail().getUsername())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        PartnerInfo partnerInfo = partnerInfoRepository.findByUserId(user.getId())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        List<Hotel> hotels = hotelsRepository.findAllByPartnerId(partnerInfo.getId());
        List<RoomPVO> roomPVOS = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        hotels.forEach((h)->{
            rooms.addAll(roomRepository.findAllByHotelId(h.getId()));
        });

        rooms.forEach((r)->{
            roomPVOS.add(convertToRoomPVO(r));
        });
        return roomPVOS;
    }

    @Override
    public List<RoomVO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomVO> roomVOS = new ArrayList<>();
        rooms.forEach((r)->{
            roomVOS.add(roomRepository.getRoomByRoomId(r.getId()).orElseThrow(()->new BusinessException(ErrorCode.ROOM_NOT_EXIST)));
        });
        return roomVOS;
    }

    @Override
    public List<RoomVO> findRoomByRequire(RoomFindDTO roomFindDTO) {

        if (roomFindDTO.getCheckIn().isAfter(roomFindDTO.getCheckOut()) ||
                roomFindDTO.getCheckIn().equals(roomFindDTO.getCheckOut()))
        {throw new BusinessException(ErrorCode.INVALID_DATE);}

        List<Hotel> hotels = hotelsRepository.getHotelByContent(roomFindDTO.getContent());

        List<Long> hotelIds = hotels.stream()
                .map(Hotel::getId)
                .toList();

        if (hotelIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Room> rooms = roomRepository.findAvailableRooms(hotelIds, roomFindDTO.getCheckIn(), roomFindDTO.getCheckOut(),ActiveStatus.ACTIVE, PaymentStatus.CANCELLED, PaymentStatus.NONE);

        return rooms.stream()
                .map(room -> roomRepository
                        .getRoomByRoomId(room.getId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_EXIST)))
                .toList();
    }

    @Override
    public RoomVO convertToRoomVO(Room room) {
        RoomVO roomVO = new RoomVO();
        roomVO.setId(room.getId()!=null?room.getId():null);
        roomVO.setRoomName(room.getRoomName());
        roomVO.setPrice(room.getPrice());
        roomVO.setCapacity(room.getCapacity());
        roomVO.setActiveStatus(room.getActiveStatus());
        roomVO.setQualityEnum(room.getQualityEnum());
        roomVO.setHotelName(hotelsRepository.getHotelNameById(room.getHotelId()));
        roomVO.setHotelId(room.getHotelId());
        roomVO.setDescription(room.getDescription());
        return roomVO;
    }

    @Override
    public RoomPVO convertToRoomPVO(Room room) {
        RoomPVO roomPVO = new RoomPVO();
        roomPVO.setRoomName(room.getRoomName());
        roomPVO.setRoomId(room.getId());
        roomPVO.setHotelId(room.getHotelId());
        roomPVO.setHotelName(hotelsRepository.findById(room.getHotelId()).orElseThrow(()->new BusinessException(ErrorCode.HOTEL_NOT_EXIST)).getHotelName());
        Image image = imageRepository.findById(room.getImageId())
                .orElseThrow(()->new BusinessException(ErrorCode.IMAGE_NOT_EXIST));
        roomPVO.setUrl(image.getUrl());
        roomPVO.setQualityEnum(room.getQualityEnum());
        roomPVO.setPrice(room.getPrice());
        roomPVO.setDescription(room.getDescription());
        roomPVO.setCapacity(room.getCapacity());
        roomPVO.setActiveStatus(room.getActiveStatus());
        return roomPVO;
    }
}
