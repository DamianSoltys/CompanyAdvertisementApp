package local.project.Inzynierka.servicelayer.promotionitem;

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

    }

    @Override
    public void schedule(Sendable sendable) {

    }

    @Override
    public void cancel() {

    }
}
