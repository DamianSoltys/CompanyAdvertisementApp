package local.project.Inzynierka.servicelayer.social.facebook.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacebookPostWithPhotosEvent {
    private SimpleFacebookPostEvent simpleFacebookPostEvent;
    private Collection<String> photoURLs;
}
