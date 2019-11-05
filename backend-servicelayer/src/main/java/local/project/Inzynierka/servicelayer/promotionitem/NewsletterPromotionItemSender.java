package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.servicelayer.newsletter.event.CreatingNewsletterMailEvent;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configurable(dependencyCheck = true, autowire = Autowire.BY_TYPE, preConstruction = true)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewsletterPromotionItemSender implements PromotionItemSender {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public void send(Sendable sendable) {

        applicationEventPublisher.publishEvent(new CreatingNewsletterMailEvent(sendable.getHTMLContent(),
                                                                               sendable.getTitle(),
                                                                               sendable.getAppUrl(),
                                                                               sendable.getCompanyId()
        ));
    }

    @Override
    public void schedule(Sendable sendable) {
        threadPoolTaskScheduler.schedule(()-> {
            applicationEventPublisher.publishEvent(new CreatingNewsletterMailEvent(sendable.getHTMLContent(),
                                                                                   sendable.getTitle(),
                                                                                   sendable.getAppUrl(),
                                                                                   sendable.getCompanyId()
            ));
        }, sendable.startTime());
    }

    @Override
    public void cancel() {

    }
}
