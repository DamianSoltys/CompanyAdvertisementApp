package local.project.Inzynierka.servicelayer.promotionitem.event;

import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendingEvent {
    private String promotionItemUUUID;
    private Destination destination;
    private SendingStatus sendingStatus;
    private String detail;
}
