package local.project.Inzynierka.web.newsletter.event;

import local.project.Inzynierka.persistence.entity.NewsletterSubscription;
import lombok.ToString;

@ToString
public class OnNewsletterSignUpEvent {


    private final NewsletterSubscription newsletterSubscription;
    private final String appUrl;

    public OnNewsletterSignUpEvent(NewsletterSubscription newsletterSubscription, String appUrl) {
        this.appUrl = appUrl;
        this.newsletterSubscription = newsletterSubscription;
    }

    public NewsletterSubscription getNewsletterSubscription() {
        return newsletterSubscription;
    }

    public String getAppUrl() {
        return appUrl;
    }

}
