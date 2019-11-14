package local.project.Inzynierka.servicelayer.dto.company;

import local.project.Inzynierka.servicelayer.dto.branch.BranchBuildDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CompanyBuildDto {
    private Long id;
    private String getLogoURL;
    private String putLogoURL;
    private String logoKey;
    private List<BranchBuildDto> branchBuildDTOs;
}
