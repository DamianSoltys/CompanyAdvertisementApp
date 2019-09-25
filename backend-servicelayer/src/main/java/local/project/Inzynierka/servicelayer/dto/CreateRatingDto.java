package local.project.Inzynierka.servicelayer.dto;

import lombok.Data;

@Data
public class CreateRatingDto {

    private Integer rating;
    private Long branchId;
}
