package local.project.Inzynierka.servicelayer.dto;

import lombok.Data;

@Data
public class PersistedBranchDto {

    private String name;
    private Address address;
    private float geoX;
    private float geoY;
    private Long branchId;
}
