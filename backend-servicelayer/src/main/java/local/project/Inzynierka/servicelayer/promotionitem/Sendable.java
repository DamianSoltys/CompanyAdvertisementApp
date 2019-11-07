package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;

import java.time.Instant;
import java.util.Set;

public interface Sendable {

    Long getCompanyId();
    String getAppUrl();
    String getUUID();
    String getContent();
    String getHTMLContent();
    String getTitle();
    Instant startTime();

    Set<Destination> getDestinations();
}
