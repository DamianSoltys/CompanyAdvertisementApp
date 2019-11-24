package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStatus;
import local.project.Inzynierka.servicelayer.promotionitem.event.SendingEvent;
import local.project.Inzynierka.servicelayer.social.facebook.event.FacebookPostWithPhotosEvent;
import local.project.Inzynierka.servicelayer.social.facebook.event.SimpleFacebookPostEvent;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configurable(dependencyCheck = true, autowire = Autowire.BY_TYPE, preConstruction = true)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FacebookPromotionItemSender implements PromotionItemSender {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public void send(Sendable sendable) {
        publishSendable(sendable);
    }

    private void publishSendable(Sendable sendable) {

        if (sendable.getPhotoURLs().isEmpty()) {
            applicationEventPublisher.publishEvent(getSingleFacebookPost(sendable));
        } else {
            applicationEventPublisher.publishEvent(FacebookPostWithPhotosEvent.builder()
                                                           .simpleFacebookPostEvent(getSingleFacebookPost(sendable))
                                                           .photoURLs(sendable.getPhotoURLs())
                                                           .build());
        }

    }

    private SimpleFacebookPostEvent getSingleFacebookPost(Sendable sendable) {
        String content = sendable.getContent();
        Long companyId = sendable.getCompanyId();
        String promotionItemUUID = sendable.getUUID();
        return SimpleFacebookPostEvent.builder()
                .content(content)
                .companyId(companyId)
                .promotionItemUUID(promotionItemUUID)
                .build();
    }

    @Override
    public void schedule(Sendable sendable) {
        applicationEventPublisher.publishEvent(SendingEvent.builder()
                                                       .destination(Destination.FB)
                                                       .sendingStatus(SendingStatus.DELAYED)
                                                       .promotionItemUUUID(sendable.getUUID())
                                                       .build());
        threadPoolTaskScheduler.schedule(() -> {
            publishSendable(sendable);
        }, sendable.getPlannedSendingTime());
    }

    @Override
    public void cancel() {

    }
}
