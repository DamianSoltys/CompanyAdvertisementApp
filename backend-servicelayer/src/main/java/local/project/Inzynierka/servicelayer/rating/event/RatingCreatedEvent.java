package local.project.Inzynierka.servicelayer.rating.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RatingCreatedEvent {
    private Integer rating;
    private Long branchId;
    private String userName;
}
