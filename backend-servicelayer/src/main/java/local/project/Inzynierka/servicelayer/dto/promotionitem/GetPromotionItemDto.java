package local.project.Inzynierka.servicelayer.dto.promotionitem;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetPromotionItemDto {
    private String promotionItemUUID;
    private SendingStatus sendingStatus;
    private List<Destination> destinations;
    private Long addedTime;
    private Long plannedSendingTime;
    private Long sendTime;
    private String name;
}
