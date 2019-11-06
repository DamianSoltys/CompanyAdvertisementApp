package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.servicelayer.newsletter.EmailMimeType;
import local.project.Inzynierka.servicelayer.newsletter.event.CreatingNewsletterMailEvent;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.StringUtils;

@Configurable(dependencyCheck = true, autowire = Autowire.BY_TYPE, preConstruction = true)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewsletterPromotionItemSender implements PromotionItemSender {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public void send(Sendable sendable) {

        publishSendable(sendable);
    }

    @Override
    public void schedule(Sendable sendable) {
        threadPoolTaskScheduler.schedule(()-> {
            publishSendable(sendable);
        }, sendable.startTime());
    }

    private void publishSendable(Sendable sendable) {
        EmailMimeType emailMimeType = StringUtils.isEmpty(sendable.getHTMLContent()) ? EmailMimeType.TEXT : EmailMimeType.HTML;
        String message = emailMimeType.equals(EmailMimeType.HTML) ? sendable.getHTMLContent() : sendable.getContent();
        applicationEventPublisher.publishEvent(new CreatingNewsletterMailEvent(message,
                                                                               sendable.getTitle(),
                                                                               sendable.getAppUrl(),
                                                                               sendable.getCompanyId(),
                                                                               emailMimeType));
    }

    @Override
    public void cancel() {

    }
}
