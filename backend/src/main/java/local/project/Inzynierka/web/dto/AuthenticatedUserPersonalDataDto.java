package local.project.Inzynierka.web.dto;

import lombok.Data;

@Data
public class AuthenticatedUserPersonalDataDto {

    String firstName;
    String lastName;
    Address address;
    String phoneNo;

}
