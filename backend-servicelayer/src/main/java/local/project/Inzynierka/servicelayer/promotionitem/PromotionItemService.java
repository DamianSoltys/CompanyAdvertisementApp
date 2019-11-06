package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.persistence.repository.PromotionItemRepository;
import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionItemService {

    private final PromotionItemRepository promotionItemRepository;

    public PromotionItemService(PromotionItemRepository promotionItemRepository) {
        this.promotionItemRepository = promotionItemRepository;
    }

    public void addPromotionItem(PromotionItemAddedEvent promotionItemAddedEvent) {

        List<PromotionItemSender> senders = buildSenders(promotionItemAddedEvent);

        for (var sender : senders) {
            if (SendingStrategy.DELAYED.equals(promotionItemAddedEvent.getSendingStrategy())) {
                sender.schedule(promotionItemAddedEvent);
            } else if (SendingStrategy.AT_CREATION.equals(promotionItemAddedEvent.getSendingStrategy())) {
                sender.send(promotionItemAddedEvent);
            } else if (SendingStrategy.AT_WILL.equals(promotionItemAddedEvent.getSendingStrategy())) {
                sender.schedule(promotionItemAddedEvent);
            }
        }
    }

    private List<PromotionItemSender> buildSenders(PromotionItemAddedEvent promotionItemAddedEvent) {
        List<PromotionItemSender> senders = new ArrayList<>();

        if (promotionItemAddedEvent.getDestination().contains(Destination.FB)) {
            var fbSender = new FacebookPromotionItemSender();
            senders.add(fbSender);
        }
        if (promotionItemAddedEvent.getDestination().contains(Destination.TWITTER)) {
            var twitterSender = new TwitterPromotionItemSender();
            senders.add(twitterSender);
        }
        if (promotionItemAddedEvent.getDestination().contains(Destination.NEWSLETTER)) {
            var newsletterSender = new NewsletterPromotionItemSender();
            senders.add(newsletterSender);
        }
        return senders;
    }
}
