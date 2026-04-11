package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.auth.entity.User;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.*;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.enumration.PaymentStatus;
import project.backend.hotel_booking.enumration.RoleEnum;
import project.backend.hotel_booking.model.dto.OrderRoomDTO;
import project.backend.hotel_booking.model.vo.OrderRoomPartnerVO;
import project.backend.hotel_booking.model.vo.OrderRoomUserVO;
import project.backend.hotel_booking.model.vo.RoomVO;
import project.backend.hotel_booking.repository.*;
import project.backend.hotel_booking.repository.OrderRoomRepository;
import project.backend.hotel_booking.repository.RoomRepository;
import project.backend.hotel_booking.service.NotificationService;
import project.backend.hotel_booking.service.OrderRoomService;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderRoomServiceImplement implements OrderRoomService {
    private final OrderRoomRepository orderRoomRepository;
    private final RoomRepository roomRepository;
    private final HotelsRepository hotelsRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final PartnerInfoRepository partnerInfoRepository;
    private final NotificationService notificationService;
    public OrderRoomServiceImplement(NotificationService notificationService,OrderRoomRepository orderRoomRepository, RoomRepository roomRepository, HotelsRepository hotelsRepository, UserInfoRepository userInfoRepository, UserRepository userRepository, PartnerInfoRepository partnerInfoRepository) {
        this.orderRoomRepository = orderRoomRepository;
        this.roomRepository = roomRepository;
        this.hotelsRepository = hotelsRepository;
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
        this.partnerInfoRepository = partnerInfoRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void createOrderRoom(OrderRoomDTO orderRoomDTO) {
        OrderRoom orderRoom = new OrderRoom();
        orderRoom.setUserName(ThreadContext.getUserDetail().getUsername());
        orderRoom.setHotelId(orderRoomDTO.getHotelId());
        orderRoom.setRoomId(orderRoomDTO.getRoomId());
        Room room = roomRepository.findById(orderRoom.getRoomId())
                .orElseThrow(()->new BusinessException(ErrorCode.ROOM_NOT_EXIST));
        orderRoom.setCapacity(room.getCapacity());
        orderRoom.setQualityEnum(room.getQualityEnum());
        orderRoom.setPrice(room.getPrice());
        orderRoom.setStartDate(orderRoomDTO.getStartDate());
        orderRoom.setEndDate(orderRoomDTO.getEndDate());
        orderRoom.prePersist();
        orderRoomRepository.save(orderRoom);
    }

    @Override
    public void depositOrderRoom(Long orderId) {
        OrderRoom orderRoom = orderRoomRepository.findById(orderId)
                .orElseThrow(()->new BusinessException(ErrorCode.ORDER_NOT_EXIST));
        orderRoom.setPaymentStatus(PaymentStatus.DEPOSITED);
        notificationService.createNotification("Đặt cọc thành công đơn hàng "+String.format("DH%3d",orderId));
        orderRoomRepository.save(orderRoom);
    }

    @Transactional
    @Override
    public void payOrderRoom(Long orderId) {
        OrderRoom orderRoom = orderRoomRepository.findById(orderId)
                .orElseThrow(()->new BusinessException(ErrorCode.ORDER_NOT_EXIST));
        orderRoom.setPaymentStatus(PaymentStatus.PAID);
        notificationService.createNotification("Thanh toán thành công đơn hàng "+String.format("DH%3d",orderId));
        orderRoomRepository.save(orderRoom);
    }

    @Transactional
    @Override
    public void cancelOrderRoom(Long orderId) {
        OrderRoom orderRoom = orderRoomRepository.findById(orderId)
                .orElseThrow(()->new BusinessException(ErrorCode.ORDER_NOT_EXIST));
        orderRoom.setPaymentStatus(PaymentStatus.CANCELLED);
        notificationService.createNotification("Hủy thành công đơn hàng "+String.format("DH%3d",orderId));
        orderRoomRepository.save(orderRoom);
    }

    @Override
    public List<OrderRoom> getOrderByPartner() {
        System.out.println(ThreadContext.getUserDetail().getUsername());
        User user = userRepository.findByUserName(ThreadContext.getUserDetail().getUsername())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        if(!user.getRole().equals(RoleEnum.PARTNER)){
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        System.out.println(user.getId());
        PartnerInfo partner = partnerInfoRepository.findByUserId(user.getId())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        List<OrderRoom> orderRooms = new ArrayList<>();
        List<Hotel> hotels = hotelsRepository.findAllByPartnerId(partner.getId());
        for(var hotel: hotels){
            System.out.println(hotel.getId());
            List<OrderRoom> orderRoomsInHotel = orderRoomRepository.findAllByHotelId(hotel.getId());
            orderRooms.addAll(orderRoomsInHotel);
        }

        return orderRooms;
    }

    @Override
    public List<OrderRoom> getOrderByUserName() {
        User user = userRepository.findByUserName(ThreadContext.getUserDetail().getUsername())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        if(!user.getRole().equals(RoleEnum.CUSTOMER)){
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        List<OrderRoom> orderRooms = orderRoomRepository.findAllByUserName(ThreadContext.getUserDetail().getUsername());
        return orderRooms;
    }

    @Override
    public OrderRoomUserVO convertToOrderRoomUserVO(OrderRoom orderRoom) {
        return null;
    }

    @Override
    public OrderRoomPartnerVO convertToOrderRoomPartnerVO(OrderRoom orderRoom) {
        return null;
    }
}
