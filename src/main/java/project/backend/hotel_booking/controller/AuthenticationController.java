package project.backend.hotel_booking.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.hotel_booking.core.auth.model.dto.LoginDTO;
import project.backend.hotel_booking.core.auth.model.dto.RegisterPartnerDTO;
import project.backend.hotel_booking.core.auth.model.dto.RegisterUserDTO;
import project.backend.hotel_booking.core.auth.service.AuthenticationService;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.enumration.ErrorCode;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register/user")
    public SuccessResponse<String> registerUser(@RequestBody RegisterUserDTO dto){
        authenticationService.registerUser(dto);
        return ResponseUtil.ok("Register success");
    }

    @PostMapping("/register/partner")
    public SuccessResponse<String> registerPartner(@RequestBody RegisterPartnerDTO dto){
        authenticationService.registerPartner(dto);
        return ResponseUtil.ok("Register success");
    }

    @PostMapping("/login")
    public SuccessResponse<String> login(@RequestBody LoginDTO dto){
        return ResponseUtil.ok(authenticationService.login(dto));
    }


    @PostMapping("/logout")
    public SuccessResponse<String> logout(HttpServletRequest request){
        String authHeader=request.getHeader("Authorization");
        if (authHeader==null || !authHeader.contains("Bearer")) {
            throw new BusinessException(ErrorCode.TOKEN_NOT_CORRECT);
        }
        authHeader=authHeader.substring(7);
        authenticationService.logout(authHeader);
        return ResponseUtil.ok("Logout success");
    }
}
