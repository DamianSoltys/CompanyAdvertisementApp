package local.project.Inzynierka.servicelayer.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class SearchableBranchDto {
    private String name;
    private Long id;
    private final String type = "Branch";
}
