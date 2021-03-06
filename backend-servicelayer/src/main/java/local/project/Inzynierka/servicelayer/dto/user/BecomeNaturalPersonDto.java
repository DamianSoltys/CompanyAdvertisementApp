package local.project.Inzynierka.servicelayer.dto.user;

import local.project.Inzynierka.servicelayer.dto.address.Address;
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
    Address address;
    String phoneNo;
}
