package project.backend.hotel_booking.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.entity.Image;
import project.backend.hotel_booking.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("api/image")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("create")
    SuccessResponse<Image> uploadImage(@Param("image")MultipartFile image) throws IOException {
        return ResponseUtil.ok(
                "upload image success",
                imageService.uploadImage(image)
        );
    }

    @DeleteMapping("{imageId}")
    SuccessResponse<String> deleteImage(@PathVariable Long imageId){
        imageService.deleteImage(imageId);
        return ResponseUtil.ok(
                "delete image success"
        );
    }
}
