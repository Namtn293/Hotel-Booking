package project.backend.hotel_booking.core.util;

import lombok.Getter;
import project.backend.hotel_booking.enumration.ErrorCode;
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode=errorCode;
    }

    public String getStatus(){
        return errorCode.getCode();
    }
}
