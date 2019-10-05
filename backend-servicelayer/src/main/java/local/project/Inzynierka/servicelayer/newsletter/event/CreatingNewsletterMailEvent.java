package local.project.Inzynierka.servicelayer.newsletter.event;

import lombok.ToString;

@ToString
public class CreatingNewsletterMailEvent {

    private final String message;

    private final String subject;

    private final String appUrl;

    private final Long companyId;

    public CreatingNewsletterMailEvent(String message, String subject, String appUrl, Long companyId) {
        this.message = message;
        this.subject = subject;
        this.appUrl = appUrl;
        this.companyId = companyId;
    }

    public String getMessage() {
        return message;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getSubject() {
        return subject;
    }
}
