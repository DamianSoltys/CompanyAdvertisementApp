package local.project.Inzynierka.servicelayer.promotionitem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionItemAddedEvent implements Sendable {

    private String title;
    private String htmlContent;
    private String nonHtmlContent;
    private Long companyId;
    private Instant startTime;
    private Instant endTime;

    @NotEmpty
    private Set<Destination> destination;
    private SendingStrategy sendingStrategy;
    private Integer numberOfPhotos;

    @JsonIgnore
    private String appUrl;
    private String UUID;

    @Override
    public String getContent() {
        return nonHtmlContent;
    }

    @Override
    public String getHTMLContent() {
        return htmlContent;
    }

    @Override
    public Instant startTime() {
        return startTime;
    }
}
