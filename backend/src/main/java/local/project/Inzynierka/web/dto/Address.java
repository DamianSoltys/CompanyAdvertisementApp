package local.project.Inzynierka.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    private Voivodeship voivodeship;
    private String city;
    private String street;
    private String buildingNo;
}
