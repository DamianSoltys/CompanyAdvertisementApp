package local.project.Inzynierka.servicelayer.dto;

import lombok.Data;

@Data
public class UpdatePersonalDataDto {

    String firstName;
    String lastName;
    Address address;
    String phoneNo;
}
