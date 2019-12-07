package local.project.Inzynierka.servicelayer.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavouriteBranchPostDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long branchId;
}
