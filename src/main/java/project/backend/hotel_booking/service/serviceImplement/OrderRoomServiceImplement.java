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
import project.backend.hotel_booking.model.vo.OrderRoomDataVO;
import project.backend.hotel_booking.model.vo.OrderRoomPartnerVO;
import project.backend.hotel_booking.model.vo.OrderRoomUserVO;
import project.backend.hotel_booking.repository.*;
import project.backend.hotel_booking.repository.OrderRoomRepository;
import project.backend.hotel_booking.repository.RoomRepository;
import project.backend.hotel_booking.service.NotificationService;
import project.backend.hotel_booking.service.OrderRoomService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class OrderRoomServiceImplement implements OrderRoomService {
    private final OrderRoomRepository orderRoomRepository;
    private final RoomRepository roomRepository;
    private final HotelsRepository hotelsRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final PartnerInfoRepository partnerInfoRepository;
    private final NotificationService notificationService;
    private final ImageRepository imageRepository;
    public OrderRoomServiceImplement(OrderRoomRepository orderRoomRepository, RoomRepository roomRepository, HotelsRepository hotelsRepository, UserInfoRepository userInfoRepository, UserRepository userRepository, PartnerInfoRepository partnerInfoRepository, NotificationService notificationService, ImageRepository imageRepository) {
        this.orderRoomRepository = orderRoomRepository;
        this.roomRepository = roomRepository;
        this.hotelsRepository = hotelsRepository;
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
        this.partnerInfoRepository = partnerInfoRepository;
        this.notificationService = notificationService;
        this.imageRepository = imageRepository;
    }

    @Override
    public Long createOrderRoom(OrderRoomDTO orderRoomDTO) {
        OrderRoom orderRoom = new OrderRoom();
        orderRoom.setUserName(ThreadContext.getUserDetail().getUsername());
        orderRoom.setRoomId(orderRoomDTO.getRoomId());
        Room room = roomRepository.findById(orderRoom.getRoomId())
                .orElseThrow(()->new BusinessException(ErrorCode.ROOM_NOT_EXIST));
        orderRoom.setHotelId(room.getHotelId());
        orderRoom.setCapacity(room.getCapacity());
        orderRoom.setQualityEnum(room.getQualityEnum());
        orderRoom.setPrice(room.getPrice()*Math.abs(ChronoUnit.DAYS.between(orderRoomDTO.getStartDate(),orderRoomDTO.getEndDate())));
        orderRoom.setStartDate(orderRoomDTO.getStartDate());
        orderRoom.setEndDate(orderRoomDTO.getEndDate());
        orderRoom.prePersist();
        orderRoomRepository.save(orderRoom);
        return orderRoom.getId();
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
        System.out.println("pay ok");
        notificationService.createNotification("Thanh toán thành công đơn hàng "+String.format("DH%3d",orderId));
        orderRoomRepository.save(orderRoom);
    }

    @Transactional
    @Override
    public void cancelOrderRoom(Long orderId) {
        OrderRoom orderRoom = orderRoomRepository.findById(orderId)
                .orElseThrow(()->new BusinessException(ErrorCode.ORDER_NOT_EXIST));
        if(PaymentStatus.DEPOSITED.equals(orderRoom.getPaymentStatus())){
            Long distance = Math.abs(ChronoUnit.DAYS.between(LocalDate.now(),orderRoom.getOrderDate()));
            if(distance>3 && distance<=7){
                orderRoom.setReturnPrice(orderRoom.getPrice()*0.2);
            }else if(distance<=3){
                orderRoom.setReturnPrice(orderRoom.getPrice()*0.3);
            }else{
                orderRoom.setReturnPrice(0);
            }
            System.out.println(distance);
        }else if(PaymentStatus.PAID.equals(orderRoom.getPaymentStatus())) {
            Long distance = Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), orderRoom.getOrderDate()));
            if (distance > 3 && distance <= 7) {
                orderRoom.setReturnPrice(orderRoom.getPrice() * 0.9);
            } else if (distance <= 3) {
                orderRoom.setReturnPrice(orderRoom.getPrice());
            } else {
                orderRoom.setReturnPrice(orderRoom.getPrice() * 0.7);
            }
            System.out.println(distance);
        }
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
        orderRoomUserVO.setId(orderRoom.getId());
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
        String url = null;
        if(room.getImageId()!=null){
            Image image = imageRepository.findById(room.getImageId())
                    .orElse(null);
            if(image!=null)url = image.getUrl();
        }
        orderRoomUserVO.setUrl(url);

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

    @Override
    public List<OrderRoomDataVO> getRevenueTotal() {
        LocalDate today=LocalDate.now();
        LocalDate sevenDaysAge=today.minusDays(6);
        Map<LocalDate,Double> map=new TreeMap<>();
        for (int i=0;i<=6;i++){
            map.put(today.minusDays(i),0.0);
        }
        List<OrderRoomDataVO> list=orderRoomRepository.getRevenueTotal(sevenDaysAge,today);
        list.forEach(c->{
            map.put(c.getDate(),c.getRevenueTotal());
        });
        List<OrderRoomDataVO> voList=new ArrayList<>();
        map.forEach((c,d)->{
            voList.add(OrderRoomDataVO.builder()
                            .date(c)
                            .revenueTotal(d)
                    .build());
        });
        return voList;
    }
}
