package local.project.Inzynierka.servicelayer.dto.branch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavouriteBranchGetDto {

    @JsonProperty(value= "favouriteBranchId")
    private String favoriteBranchUUID;

    private Long branchId;

    private Long userId;
}
