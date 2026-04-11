package project.backend.hotel_booking.controller;

import org.springframework.web.bind.annotation.*;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.model.vo.UserInfoVO;
import project.backend.hotel_booking.service.UserInfoService;

import java.util.List;

@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {
    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("/get-all")
    public SuccessResponse<List<UserInfoVO>> getAllUserInfo(){
        return ResponseUtil.ok("Get success",userInfoService.getAllUserInfo());
    }

    @PostMapping("lock/{userId}")
    public SuccessResponse<String> lockUser(@PathVariable String userId){
        userInfoService.lockUser(userId);
        return ResponseUtil.ok("Lock/Unlock success");
    }

    @GetMapping("/new-account-total")
    public SuccessResponse<Long> newAccountTotal(){
        return ResponseUtil.ok("Get success", userInfoService.accountCount());
    }
}
