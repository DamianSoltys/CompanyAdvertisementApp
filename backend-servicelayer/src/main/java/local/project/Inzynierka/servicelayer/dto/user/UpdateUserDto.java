package local.project.Inzynierka.servicelayer.dto.user;

import lombok.Data;

@Data
public class UpdateUserDto {

    private String currentPassword;
    private String newPassword;
}
