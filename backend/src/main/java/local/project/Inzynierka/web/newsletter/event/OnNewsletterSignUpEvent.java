package local.project.Inzynierka.web.newsletter.event;

import local.project.Inzynierka.persistence.entity.NewsletterSubscription;
import lombok.ToString;

@ToString
public class OnNewsletterSignUpEvent {

    private final NewsletterSubscription newsletterSubscription;
    private final String appUrl;
    private final boolean verified;

    public OnNewsletterSignUpEvent(NewsletterSubscription newsletterSubscription, String appUrl, boolean verified) {
        this.appUrl = appUrl;
        this.newsletterSubscription = newsletterSubscription;
        this.verified = verified;
    }

    public NewsletterSubscription getNewsletterSubscription() {
        return newsletterSubscription;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public boolean isVerified() {
        return verified;
    }
}
