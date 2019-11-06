package local.project.Inzynierka.persistence.projections;

import org.springframework.beans.factory.annotation.Value;

public interface NewsletterSubscriptionSendEmailInfo {

    @Value("#{target.unsubscribeToken.token}")
    String getUnsubscribeToken();

    @Value("#{target.emailAddressEntity.email}")
    String getSignedUpEmail();
}
