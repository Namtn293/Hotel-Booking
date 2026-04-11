package project.backend.hotel_booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.model.vo.NotificationVO;
import project.backend.hotel_booking.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping(name = "/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/get/all")
    public SuccessResponse<List<NotificationVO>> getMyAllNotifications(){
        return ResponseUtil.ok("Get notifications success",notificationService.getMyNotification());
    }
}
