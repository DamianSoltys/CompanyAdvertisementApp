package local.project.Inzynierka.servicelayer.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class BranchBuildDto {
    private Long id;
    private String getLogoURL;
    private String putLogoURL;
    private String logoKey;
}
