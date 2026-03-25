package project.backend.hotel_booking.core.util;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private String status;

    public ErrorResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }
}
