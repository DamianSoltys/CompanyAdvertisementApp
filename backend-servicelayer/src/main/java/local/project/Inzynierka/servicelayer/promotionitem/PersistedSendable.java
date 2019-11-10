package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersistedSendable implements Sendable {
    private Long companyId;
    private String name;
    private String appUrl;
    private String UUID;
    private String content;
    private String htmlContent;
    private String emailTitle;
    private Instant plannedSendingTime;
    private Collection<Destination> destinations;
    private SendingStrategy sendingStrategy;
    private Collection<String> photoURLs;

    @Override
    public String getHTMLContent() {
        return htmlContent;
    }

    @Override
    public Collection<String> getPhotoURLs() {
        return photoURLs;
    }

}
