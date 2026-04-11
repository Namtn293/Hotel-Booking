package project.backend.hotel_booking.service.serviceImplement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.backend.hotel_booking.core.util.BusinessException;
import project.backend.hotel_booking.entity.Image;
import project.backend.hotel_booking.enumration.ErrorCode;
import project.backend.hotel_booking.repository.ImageRepository;
import project.backend.hotel_booking.service.ImageService;

import java.util.Map;

@Service
public class ImageServiceImplement implements ImageService {
    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;

    public ImageServiceImplement(Cloudinary cloudinary, ImageRepository imageRepository) {
        this.cloudinary = cloudinary;
        this.imageRepository = imageRepository;
    }

    @Override
    public Image uploadImage(MultipartFile image) throws  RuntimeException {
        Image newImage = new Image();
        try {
            Map result = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.emptyMap()
            );
            newImage.setUrl(result.get("url").toString());
            newImage.setPublicId(result.get("public_id").toString());
            imageRepository.save(newImage);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.FAIL_UPLOAD_IMAGE);
        }

        return newImage;
    }

    @Override
    public void deleteImage(Long imageId) {
        try {
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(()->new BusinessException(ErrorCode.IMAGE_NOT_EXIST));
            Map result = cloudinary.uploader().destroy(
                    image.getPublicId(),
                    ObjectUtils.emptyMap()
            );
            imageRepository.delete(image);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.FAIL_DELETE_IMAGE);
        }
    }
}
