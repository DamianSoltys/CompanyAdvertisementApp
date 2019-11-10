package local.project.Inzynierka.servicelayer.promotionitem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PromotionItemSentEvent {
    private String promotionItemUUID;
}
