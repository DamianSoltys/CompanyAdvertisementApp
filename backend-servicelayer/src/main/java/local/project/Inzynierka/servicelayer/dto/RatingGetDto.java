package local.project.Inzynierka.servicelayer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RatingGetDto {
    private Integer rating;
    private Long ratingId;
    private Long userId;
    private Long branchId;
}
