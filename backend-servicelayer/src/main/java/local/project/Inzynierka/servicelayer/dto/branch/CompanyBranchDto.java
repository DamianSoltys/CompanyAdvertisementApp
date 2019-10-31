package local.project.Inzynierka.servicelayer.dto.branch;

import local.project.Inzynierka.servicelayer.dto.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyBranchDto {

    private String name;

    private Address address;

    private Double geoX;
    private Double geoY;
    private String logoURL;
    private String logoKey;
    private Boolean hasLogoAdded;
}
