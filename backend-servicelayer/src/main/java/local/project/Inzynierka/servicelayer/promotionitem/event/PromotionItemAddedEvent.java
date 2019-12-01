package local.project.Inzynierka.servicelayer.promotionitem.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.persistence.constants.SendingStrategy;
import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemType;
import local.project.Inzynierka.servicelayer.promotionitem.Sendable;
import local.project.Inzynierka.servicelayer.promotionitem.validation.AtLeastNMinutesDelay;
import local.project.Inzynierka.servicelayer.promotionitem.validation.AtLeastOneContent;
import local.project.Inzynierka.servicelayer.promotionitem.validation.HtmlContentBase64Encoded;
import local.project.Inzynierka.servicelayer.promotionitem.validation.LackOfDelayTimeWhenNotDelayedStrategy;
import local.project.Inzynierka.servicelayer.promotionitem.validation.RequiredTextContentWhenPostingToSocialMedia;
import local.project.Inzynierka.servicelayer.promotionitem.validation.RequiredTitleWhenNewsletter;
import local.project.Inzynierka.servicelayer.promotionitem.validation.PlannedSendingTimeRequiredWhenDelayed;
import local.project.Inzynierka.servicelayer.promotionitem.validation.ValidContentSize;
import local.project.Inzynierka.servicelayer.promotionitem.validation.ZeroOrMaximum5Photos;
import local.project.Inzynierka.servicelayer.validation.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AtLeastOneContent
@AtLeastNMinutesDelay
@RequiredTextContentWhenPostingToSocialMedia
@ZeroOrMaximum5Photos
@HtmlContentBase64Encoded
@RequiredTitleWhenNewsletter
@PlannedSendingTimeRequiredWhenDelayed
@LackOfDelayTimeWhenNotDelayedStrategy
@ValidContentSize
public class    PromotionItemAddedEvent implements Sendable {

    @NotEmpty
    private String name;
    private String emailTitle;
    private String htmlContent;

    @NullOrNotBlank
    private String nonHtmlContent;

    @NotNull
    private Long companyId;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Instant plannedSendingTime;

    @NotEmpty
    private Set<Destination> destinations;

    @NotNull
    private SendingStrategy sendingStrategy;

    private Integer numberOfPhotos;

    @JsonIgnore
    private String appUrl;

    @JsonIgnore
    private String UUID;

    @NotNull
    private PromotionItemType promotionItemType;

    @Override
    @JsonIgnore
    public String getContent() {
        return nonHtmlContent;
    }

    @Override
    @JsonIgnore
    public String getHTMLContent() {
        return htmlContent;
    }

}
