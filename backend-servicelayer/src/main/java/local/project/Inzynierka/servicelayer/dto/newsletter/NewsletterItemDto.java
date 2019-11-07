package local.project.Inzynierka.servicelayer.dto.newsletter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsletterItemDto {

    @NotBlank
    String message;

    @NotBlank
    String subject;
}
