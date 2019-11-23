package local.project.Inzynierka.servicelayer.social.facebook.fbapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookLogin {
    private Status status;
    private AuthResponse authResponse;
    private Long companyId;
}
