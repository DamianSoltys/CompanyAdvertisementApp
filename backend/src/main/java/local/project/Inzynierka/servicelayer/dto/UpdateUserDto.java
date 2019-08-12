package local.project.Inzynierka.servicelayer.dto;

import lombok.Data;

@Data
public class UpdateUserDto {

    private String currentPassword;
    private String newPassword;
}
