package local.project.Inzynierka.servicelayer.rating.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RatingEditedEvent {
    private Long ratingId;
    private Integer rating;
}
