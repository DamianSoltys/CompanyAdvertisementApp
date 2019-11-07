package local.project.Inzynierka.servicelayer.dto.user;

import local.project.Inzynierka.servicelayer.dto.address.Address;
import lombok.Data;

@Data
public class UpdatePersonalDataDto {

    String firstName;
    String lastName;
    Address address;
    String phoneNo;
}
