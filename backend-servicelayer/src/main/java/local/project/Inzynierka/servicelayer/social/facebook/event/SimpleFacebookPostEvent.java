package local.project.Inzynierka.servicelayer.social.facebook.event;

import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleFacebookPostEvent {
    private String content;
    private Long companyId;
    private String promotionItemUUID;
    private Destination destination;
}
