package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import lombok.*;
import project.backend.hotel_booking.core.util.EntityBase;
@Entity(name = "MAIN_IMAGE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Image extends EntityBase {
    private String url;
    private String publicId;
}
