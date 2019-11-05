package local.project.Inzynierka.servicelayer.dto.promotionitem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddPromotionItemDto {

    private String name;
    private String content;
    private PromotionItemType promotionItemType;
    private Destination destination;
}
