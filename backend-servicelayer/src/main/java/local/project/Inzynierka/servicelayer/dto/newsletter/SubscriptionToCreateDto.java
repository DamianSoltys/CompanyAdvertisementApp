package local.project.Inzynierka.servicelayer.dto.newsletter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionToCreateDto {

    String emailToSignUp;
    Long id;
    boolean verified;
}
