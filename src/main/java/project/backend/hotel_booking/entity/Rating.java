package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.core.util.EntityBase;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rating extends EntityBase {
    private Long rating;
    private String reason;
    private LocalDate createdDate;
    private Long hotelId;
    private Long userId;

    @PrePersist
    public void prePersist(){
        this.createdDate=LocalDate.now();
    }
}
