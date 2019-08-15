package local.project.Inzynierka.servicelayer.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class AuthenticatedUserInfoDto {

    private Long naturalPersonID;
    private String emailAddress;
    private String loginName;
    private Collection<Long> companiesIDs;
}
