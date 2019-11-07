package local.project.Inzynierka.servicelayer.newsletter.event;

import local.project.Inzynierka.servicelayer.newsletter.EmailMimeType;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class CreatingNewsletterMailEvent {

    private final String message;

    private final String subject;

    private final String appUrl;

    private final Long companyId;

    private final EmailMimeType emailMimeType;

    public CreatingNewsletterMailEvent(String message, String subject, String appUrl, Long companyId, EmailMimeType emailMimeType) {
        this.message = message;
        this.subject = subject;
        this.appUrl = appUrl;
        this.companyId = companyId;
        this.emailMimeType = emailMimeType;
    }

}
