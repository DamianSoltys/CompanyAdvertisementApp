package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.persistence.constants.SendingStrategy;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

public interface Sendable {

    Long getCompanyId();
    String getAppUrl();
    String getUUID();
    String getName();
    String getContent();
    String getHTMLContent();
    String getEmailTitle();
    Instant getPlannedSendingTime();

    SendingStrategy getSendingStrategy();

    Collection<Destination> getDestinations();

    default Collection<String> getPhotoURLs() {
        return Collections.emptyList();
    }
}
