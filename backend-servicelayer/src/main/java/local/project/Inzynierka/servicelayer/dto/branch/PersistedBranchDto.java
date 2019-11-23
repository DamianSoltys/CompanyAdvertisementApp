package local.project.Inzynierka.servicelayer.dto.branch;

import local.project.Inzynierka.servicelayer.dto.address.Address;
import lombok.Data;

@Data
public class PersistedBranchDto {

    private String name;
    private Address address;
    private Double geoX;
    private Double geoY;
    private Long branchId;
    private Long companyId;
    private String getLogoURL;
    private String putLogoURL;
    private String logoKey;
    private Boolean hasLogoAdded;
    private String category;
}
