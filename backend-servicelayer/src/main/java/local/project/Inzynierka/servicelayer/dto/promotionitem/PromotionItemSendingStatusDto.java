package local.project.Inzynierka.servicelayer.dto.promotionitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionItemSendingStatusDto {

    private String promotionItemUUID;
    private List<String> promotionItemPhotosUUIDsDto;
    private Boolean sendingFinished;
}
