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

import java.time.LocalDate;
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
    public OrderRoomServiceImplement(OrderRoomRepository orderRoomRepository, RoomRepository roomRepository, HotelsRepository hotelsRepository, UserInfoRepository userInfoRepository, UserRepository userRepository, PartnerInfoRepository partnerInfoRepository, NotificationService notificationService) {
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
    public List<OrderRoomPartnerVO> getOrderByPartner() {
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
        List<OrderRoomPartnerVO> orderRoomPartnerVOS = new ArrayList<>();
        List<Hotel> hotels = hotelsRepository.findAllByPartnerId(partner.getId());
        for(var hotel: hotels){
            System.out.println(hotel.getId());
            List<OrderRoom> orderRoomsInHotel = orderRoomRepository.findAllByHotelId(hotel.getId());
            orderRooms.addAll(orderRoomsInHotel);
        }

        orderRooms.forEach((or)->{
            orderRoomPartnerVOS.add(convertToOrderRoomPartnerVO(or));
        });

        return orderRoomPartnerVOS;
    }

    @Override
    public List<OrderRoomUserVO> getOrderByUserName() {
        User user = userRepository.findByUserName(ThreadContext.getUserDetail().getUsername())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        if(!user.getRole().equals(RoleEnum.CUSTOMER)){
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        List<OrderRoom> orderRooms = orderRoomRepository.findAllByUserName(ThreadContext.getUserDetail().getUsername());
        List<OrderRoomUserVO> orderRoomUserVOS = new ArrayList<>();
        orderRooms.forEach((or)->{
            orderRoomUserVOS.add(convertToOrderRoomUserVO(or));
        });
        return orderRoomUserVOS;
    }

    @Override
    public OrderRoomUserVO convertToOrderRoomUserVO(OrderRoom orderRoom) {
        OrderRoomUserVO orderRoomUserVO = new OrderRoomUserVO();
        orderRoomUserVO.setRoomId(orderRoom.getRoomId());
        orderRoomUserVO.setPrice(orderRoom.getPrice());
        orderRoomUserVO.setStartDate(orderRoom.getStartDate());
        orderRoomUserVO.setEndDate(orderRoom.getEndDate());
        orderRoomUserVO.setPaymentStatus(orderRoom.getPaymentStatus());
        orderRoomUserVO.setQualityEnum(orderRoom.getQualityEnum());

        Room room = roomRepository.findById(orderRoom.getRoomId())
                .orElseThrow(()->new BusinessException(ErrorCode.ROOM_NOT_EXIST));
        orderRoomUserVO.setRoomName(room.getRoomName());

        Hotel hotel = hotelsRepository.findById(room.getHotelId())
                .orElseThrow(()->new BusinessException(ErrorCode.HOTEL_NOT_EXIST));
        orderRoomUserVO.setHotelName(hotel.getHotelName());
        orderRoomUserVO.setAddress(hotel.getAddress());


        return orderRoomUserVO;
    }

    @Override
    public OrderRoomPartnerVO convertToOrderRoomPartnerVO(OrderRoom orderRoom) {
        OrderRoomPartnerVO orderRoomPartnerVO = new OrderRoomPartnerVO();
        orderRoomPartnerVO.setUserName(orderRoom.getUserName());
        orderRoomPartnerVO.setCapacity(orderRoom.getCapacity());
        orderRoomPartnerVO.setQualityEnum(orderRoom.getQualityEnum());
        orderRoomPartnerVO.setPrice(orderRoom.getPrice());
        orderRoomPartnerVO.setOrderDate(orderRoom.getOrderDate());
        Room room = roomRepository.findById(orderRoom.getRoomId())
                .orElseThrow(()->new BusinessException(ErrorCode.ROOM_NOT_EXIST));
        orderRoomPartnerVO.setRoomName(room.getRoomName());
        return orderRoomPartnerVO;
    }

    @Override
    public Double getTodayRevenue() {
        return orderRoomRepository.getTodayRevenue(LocalDate.now());
    }

    @Override
    public Long getTodayReservedOrder() {
        return orderRoomRepository.getTodayReversedOrder(LocalDate.now());
    }
}
