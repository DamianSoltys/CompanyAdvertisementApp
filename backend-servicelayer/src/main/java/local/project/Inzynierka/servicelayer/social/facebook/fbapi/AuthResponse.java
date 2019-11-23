package local.project.Inzynierka.servicelayer.social.facebook.fbapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private Long data_access_expiration_time;
    private Integer expiresIn;
    private String signedRequest;
    private Long userID;
}
