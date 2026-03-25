package project.backend.hotel_booking.core.auth.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.backend.hotel_booking.core.auth.entity.Token;
import project.backend.hotel_booking.core.auth.entity.User;
import project.backend.hotel_booking.core.auth.model.dto.LoginDTO;
import project.backend.hotel_booking.core.auth.model.dto.RegisterDTO;
import project.backend.hotel_booking.core.auth.repository.TokenRepository;
import project.backend.hotel_booking.core.auth.repository.UserRepository;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.enumration.RoleEnum;

import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthenticationService(TokenRepository tokenRepository,JwtService jwtService,UserRepository userRepository,PasswordEncoder passwordEncoder,UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void register(RegisterDTO registerDTO) throws BusinessException {
        if (userRepository.existsByUserName(registerDTO.getUserName())){
            throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);
        }
        User user=new User();
        user.setUserName(registerDTO.getUserName());
        user.setRoleEnum(RoleEnum.CUSTOMER);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userRepository.save(user);

//        imageService.createImage(null,null);

    }

    public String login( LoginDTO dto)throws BusinessException{
        User user=userRepository.findByUserName(dto.getUserName())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_ALREADY_EXIST));
        if (!passwordEncoder.matches(dto.getPassword(),user.getPassword())){
            throw new BusinessException(ErrorCode.PASSWORD_NOT_CORRECT);
        }
        System.out.println("1");
        String token=jwtService.buildAccessToken(user);
        revokedToken(user.getId());
        System.out.println("2");

        Token token1=Token.builder()
                .token(token)
                .userId(user.getId())
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token1);
        return token;
    }

    public void revokedToken(Long userId){
        List<Token> tokens=tokenRepository.findByUserId(userId);
        if (tokens.isEmpty()) return;
        tokens.forEach(c->{
            c.setRevoked(true);
            c.setExpired(true);
            tokenRepository.save(c);
        });
    }

    public void logout(String token)throws BusinessException{
        Token jwt=tokenRepository.findByToken(token).orElseThrow(
                ()-> new BusinessException(ErrorCode.TOKEN_NOT_EXIST));
        jwt.setExpired(true);
        jwt.setRevoked(true);
        tokenRepository.save(jwt);
    }
}
