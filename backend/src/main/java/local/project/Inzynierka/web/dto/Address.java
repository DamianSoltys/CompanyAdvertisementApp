package local.project.Inzynierka.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @NotNull
    private Voivodeship voivodeship;

    @NotEmpty
    private String city;

    private String street;

    @NotEmpty
    private String buildingNo;

    String apartmentNo;
}
