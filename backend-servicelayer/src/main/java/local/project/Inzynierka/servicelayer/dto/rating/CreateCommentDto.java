package local.project.Inzynierka.servicelayer.dto.rating;

import lombok.Data;

@Data
public class CreateCommentDto {

    private String comment;
    private Long branchId;
}
