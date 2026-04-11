package project.backend.hotel_booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.hotel_booking.core.util.EntityBase;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification extends EntityBase {
    private Long userId;
    private String content;
    private LocalDateTime createdTime;

    @PrePersist
    void prePersist(){
        this.createdTime=LocalDateTime.now();
    }
}
