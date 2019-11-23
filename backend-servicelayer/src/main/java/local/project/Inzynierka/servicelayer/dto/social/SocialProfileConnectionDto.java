package local.project.Inzynierka.servicelayer.dto.social;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialProfileConnectionDto {
    private ConnectionStatus connectionStatus;
    private SocialPlatform socialPlatform;
}
