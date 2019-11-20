package local.project.Inzynierka.servicelayer.social.facebook.event;

import local.project.Inzynierka.servicelayer.social.facebook.fbapi.FacebookLogin;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.TokenInspection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacebookTokenInstalledEvent {
    private TokenInspection tokenInspection;
    private FacebookLogin facebookLogin;
    private String inspectedToken;
}
