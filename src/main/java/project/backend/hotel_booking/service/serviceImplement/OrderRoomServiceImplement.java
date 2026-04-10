package project.backend.hotel_booking.service.serviceImplement;

import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.configuration.ThreadContext;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.OrderRoom;
import project.backend.hotel_booking.entity.Room;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.enumration.PaymentStatus;
import project.backend.hotel_booking.model.dto.OrderRoomDTO;
import project.backend.hotel_booking.model.vo.OrderRoomPartnerVO;
import project.backend.hotel_booking.model.vo.OrderRoomUserVO;
import project.backend.hotel_booking.model.vo.RoomVO;
import project.backend.hotel_booking.repository.OrderRoomRepository;
import project.backend.hotel_booking.repository.RoomRepository;
import project.backend.hotel_booking.service.OrderRoomService;

import java.util.List;

@Service
public class OrderRoomServiceImplement implements OrderRoomService {
    private final OrderRoomRepository orderRoomRepository;
    private final RoomRepository roomRepository;
    public OrderRoomServiceImplement(OrderRoomRepository orderRoomRepository, RoomRepository roomRepository) {
        this.orderRoomRepository = orderRoomRepository;
        this.roomRepository = roomRepository;
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
        orderRoomRepository.save(orderRoom);
    }

    @Override
    public void payOrderRoom(Long orderId) {
        OrderRoom orderRoom = orderRoomRepository.findById(orderId)
                .orElseThrow(()->new BusinessException(ErrorCode.ORDER_NOT_EXIST));
        orderRoom.setPaymentStatus(PaymentStatus.PAID);
        orderRoomRepository.save(orderRoom);
    }

    @Override
    public void cancelOrderRoom(Long orderId) {
        OrderRoom orderRoom = orderRoomRepository.findById(orderId)
                .orElseThrow(()->new BusinessException(ErrorCode.ORDER_NOT_EXIST));
        orderRoom.setPaymentStatus(PaymentStatus.CANCELLED);
        orderRoomRepository.save(orderRoom);
    }

    @Override
    public List<OrderRoom> getOrderByHotelId(Long hotelId) {
        List<OrderRoom> orderRooms = orderRoomRepository.findAllByHotelId(hotelId);
        return orderRooms;
    }

    @Override
    public List<OrderRoom> getOrderByUserName() {
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
