package local.project.Inzynierka.web.dto;

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

    private float geoX;
    private float geoY;
}
