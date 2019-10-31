package local.project.Inzynierka.servicelayer.dto.newsletter;

import local.project.Inzynierka.servicelayer.validation.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class NewSubscriptionDto {

    @NullOrNotBlank
    private String email;

    @NotNull
    private Long id;
}
