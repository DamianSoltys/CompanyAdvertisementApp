package local.project.Inzynierka.servicelayer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BecomeNaturalPersonDto {

    String firstName;
    String lastName;
    String voivodeship;
    String city;
    String street;
    String apartmentNo;
    String buildingNo;
    String phoneNo;
}
