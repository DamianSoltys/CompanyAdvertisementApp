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
public class AddBranchDto {

    private String name;

    private Address address;

    private Double geoX;
    private Double geoY;
}
