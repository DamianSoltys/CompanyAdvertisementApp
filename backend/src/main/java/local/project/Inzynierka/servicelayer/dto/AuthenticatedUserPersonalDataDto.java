package local.project.Inzynierka.servicelayer.dto;

import lombok.Data;

@Data
public class AuthenticatedUserPersonalDataDto {

    String firstName;
    String lastName;
    Address address;
    String phoneNo;

}
