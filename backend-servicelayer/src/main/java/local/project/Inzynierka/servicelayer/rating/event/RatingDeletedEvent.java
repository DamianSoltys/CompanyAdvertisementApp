package local.project.Inzynierka.servicelayer.rating.event;

import lombok.Data;

@Data
public class RatingDeletedEvent {
    private Long ratingId;
}
