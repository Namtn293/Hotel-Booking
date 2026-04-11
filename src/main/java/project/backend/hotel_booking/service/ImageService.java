package project.backend.hotel_booking.service;

import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.entity.Image;

import java.io.IOException;

public interface ImageService {
    Image uploadImage(MultipartFile image) throws IOException, RuntimeException;
    void deleteImage(Long imageId);
}
