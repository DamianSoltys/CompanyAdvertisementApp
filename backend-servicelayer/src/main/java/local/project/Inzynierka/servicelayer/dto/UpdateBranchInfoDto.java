package local.project.Inzynierka.servicelayer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBranchInfoDto {

    private String name;

    @Valid
    private Address address;

    private Float geoX;

    private Float geoY;
}
