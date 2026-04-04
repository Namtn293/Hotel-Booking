package project.backend.hotel_booking.enumration;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXIST("409","User already exist"),
    USER_NOT_ALREADY_EXIST("409","User not already exist"),
    FORBIDDEN("403","You do not have permission to perform this action"),
    TOKEN_NOT_CORRECT("401","Token not correct"),
    USER_NOT_FOUND("409","User not found"),
    PASSWORD_NOT_CORRECT("401","Password not correct"),
    TOKEN_NOT_EXIST("404","Token not exist"),
    TOKEN_EXPIRED("401","Token expired"),
    TOKEN_INVAlID("401","Token invalid"),
    PASSWORD_NOT_EQUAL("401","Password not equal"),
    PARTNER_NOT_ALREADY_EXIST("409","Partner not already exist"),
    USER_ALREADY_HAVE_RATING("409","User already have rating to this hotel"),

            ;

    private final String code;
    private final String message;
    ErrorCode(String code,String message){
        this.code=code;
        this.message=message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
