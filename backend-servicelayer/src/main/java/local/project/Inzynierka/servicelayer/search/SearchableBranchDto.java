package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.servicelayer.dto.address.Address;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
class SearchableBranchDto {
    private final String type = "Branch";
    private String name;
    private Long id;
    private Address address;
    private Long companyId;
    private String logoPath;
    private String logoKey;
    private Boolean hasLogoAdded;
    private String category;
}
