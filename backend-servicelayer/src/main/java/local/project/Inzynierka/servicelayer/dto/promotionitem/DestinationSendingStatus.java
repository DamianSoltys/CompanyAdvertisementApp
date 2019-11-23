package local.project.Inzynierka.servicelayer.dto.promotionitem;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DestinationSendingStatus {
    private SendingStatus sendingStatus;
    private String detail;
    private Destination destination;
    private Long sendAt;
    private Long plannedSendingAt;
    private Long failedAt;
}

