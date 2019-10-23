package local.project.Inzynierka.servicelayer.dto;

import lombok.Data;

@Data
public class PersistedBranchDto {

    private String name;
    private Address address;
    private Double geoX;
    private Double geoY;
    private Long branchId;
    private String logoPath;
    private String logoKey;
    private Boolean hasLogoAdded;
}
