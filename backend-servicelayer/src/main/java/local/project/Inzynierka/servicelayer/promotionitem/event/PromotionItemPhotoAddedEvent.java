package local.project.Inzynierka.servicelayer.promotionitem.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionItemPhotoAddedEvent {
    private String photoUUID;
    private String promotionItemUUID;
}
