package project.backend.hotel_booking.service;

import project.backend.hotel_booking.entity.OrderRoom;
import project.backend.hotel_booking.model.dto.OrderRoomDTO;
import project.backend.hotel_booking.model.vo.OrderRoomDataVO;
import project.backend.hotel_booking.model.vo.OrderRoomPartnerVO;
import project.backend.hotel_booking.model.vo.OrderRoomUserVO;

import java.time.LocalDate;
import java.util.List;

public interface OrderRoomService {
    void createOrderRoom(OrderRoomDTO orderRoomDTO);
    void depositOrderRoom(Long orderId);
    void payOrderRoom(Long orderId);
    void cancelOrderRoom(Long orderId);
    List<OrderRoom> getOrderByPartner();
    List<OrderRoom> getOrderByUserName();

    OrderRoomUserVO convertToOrderRoomUserVO(OrderRoom orderRoom);
    OrderRoomPartnerVO convertToOrderRoomPartnerVO(OrderRoom orderRoom);

    Double getTodayRevenue();

    Long getTodayReservedOrder();

    List<OrderRoomDataVO> getRevenueTotal();
}
