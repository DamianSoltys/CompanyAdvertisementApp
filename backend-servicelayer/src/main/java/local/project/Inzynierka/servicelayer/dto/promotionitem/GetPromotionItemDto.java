package local.project.Inzynierka.servicelayer.dto.promotionitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPromotionItemDto {
    private Long promotionItemId;
    private SendingStatus sendingStatus;
    private List<Destination> destinations;
    private Instant dateAdded;
}
