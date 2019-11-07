package local.project.Inzynierka.servicelayer.dto.user;

import lombok.Data;

import java.util.Collection;

@Data
public class UserInfoDto {

    private Long userID;
    private Long naturalPersonID;
    private String emailAddress;
    private String loginName;
    private Collection<Long> companiesIDs;
}
