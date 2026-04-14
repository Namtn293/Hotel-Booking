package project.backend.hotel_booking.controller;

import org.springframework.web.bind.annotation.*;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.entity.OrderRoom;
import project.backend.hotel_booking.enumration.PaymentStatus;
import project.backend.hotel_booking.model.dto.OrderRoomDTO;
import project.backend.hotel_booking.model.vo.OrderRoomPartnerVO;
import project.backend.hotel_booking.model.vo.OrderRoomUserVO;
import project.backend.hotel_booking.model.vo.OrderRoomDataVO;
import project.backend.hotel_booking.service.OrderRoomService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderRoomController {
    private final OrderRoomService orderRoomService;

    public OrderRoomController(OrderRoomService orderRoomService) {
        this.orderRoomService = orderRoomService;
    }

    @PostMapping("/create")
    SuccessResponse<Long> createOrderRoom(@RequestBody OrderRoomDTO orderRoomDTO){

        return ResponseUtil.ok(
                "create order success",
                orderRoomService.createOrderRoom(orderRoomDTO)
        );
    }

    @PutMapping("/deposit/{orderId}")
    SuccessResponse<PaymentStatus> depositOrderRoom(@PathVariable Long orderId){
        return ResponseUtil.ok(
                "deposit room success",
                orderRoomService.depositOrderRoom(orderId);
        );
    }

    @PutMapping("/pay/{orderId}")
    SuccessResponse<PaymentStatus> payOrderRoom(@PathVariable Long orderId){

        return ResponseUtil.ok(
                "pay order success",
                orderRoomService.payOrderRoom(orderId)
        );
    }

    @PutMapping("/cancel/{orderId}")
    SuccessResponse<String> cancelOrderRoom(@PathVariable Long orderId){
        orderRoomService.cancelOrderRoom(orderId);
        return ResponseUtil.ok(
                "cancel order success"
        );
    }

    @GetMapping("/partner")
    SuccessResponse<List<OrderRoomPartnerVO>> getOrderByPartner(){
        return ResponseUtil.ok(
                "get order success",
                orderRoomService.getOrderByPartner()
        );
    }

    @GetMapping("/user")
    SuccessResponse<List<OrderRoomUserVO>> getOrderByUserName(){
        return ResponseUtil.ok(
                "get order success",
                orderRoomService.getOrderByUserName()
        );
    }

    @GetMapping("/get-today-revenue")
    public SuccessResponse<Double> getTodayRevenue(){
        return ResponseUtil.ok("Get today revenue success",orderRoomService.getTodayRevenue());
    }

    @GetMapping("/get-today-reserved-order")
    public SuccessResponse<Long> getTodayReservedOrder(){
        return ResponseUtil.ok("Get today reserve order success",orderRoomService.getTodayReservedOrder());
    }

    @GetMapping("/get-revenue-total-each-day")
    public SuccessResponse<List<OrderRoomDataVO>> getRevenueTotalEachDay(){
        return ResponseUtil.ok("Get today reserve order success",orderRoomService.getRevenueTotal());
    }
}
