package local.project.Inzynierka.servicelayer.dto.rating;

import lombok.Data;

@Data
public class CreateRatingDto {

    private Integer rating;
    private Long branchId;
}
