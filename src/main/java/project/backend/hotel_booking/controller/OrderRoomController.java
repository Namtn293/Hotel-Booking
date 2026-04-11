package project.backend.hotel_booking.controller;

import org.springframework.web.bind.annotation.*;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.entity.OrderRoom;
import project.backend.hotel_booking.model.dto.OrderRoomDTO;
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
    SuccessResponse<String> createOrderRoom(@RequestBody OrderRoomDTO orderRoomDTO){
        orderRoomService.createOrderRoom(orderRoomDTO);
        return ResponseUtil.ok(
                "create order success"
        );
    }

    @PutMapping("/deposit/{orderId}")
    SuccessResponse<String> depositOrderRoom(@PathVariable Long orderId){
        orderRoomService.depositOrderRoom(orderId);
        return ResponseUtil.ok(
                "deposit room success"
        );
    }

    @PutMapping("/pay/{orderId}")
    SuccessResponse<String> payOrderRoom(@PathVariable Long orderId){
        orderRoomService.payOrderRoom(orderId);
        return ResponseUtil.ok(
                "pay order success"
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
    SuccessResponse<List<OrderRoom>> getOrderByPartner(){
        return ResponseUtil.ok(
                "get order success",
                orderRoomService.getOrderByPartner()
        );
    }

    @GetMapping("/user")
    SuccessResponse<List<OrderRoom>> getOrderByUserName(){
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
}
