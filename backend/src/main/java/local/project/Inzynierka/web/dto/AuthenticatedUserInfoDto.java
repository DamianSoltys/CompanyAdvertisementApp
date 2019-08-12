package local.project.Inzynierka.web.dto;

import lombok.Data;

@Data
public class AuthenticatedUserInfoDto {

    String firstName;
    String lastName;
    Address address;
    String phoneNo;

}
