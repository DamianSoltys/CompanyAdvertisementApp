package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.servicelayer.dto.address.Address;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
class SearchableCompanyDto {
    private String name;
    private Long id;
    private final String type = "Company";
    private Address address;
    private String category;
    private String logoPath;
    private String logoKey;
    private Boolean hasLogoAdded;
}
