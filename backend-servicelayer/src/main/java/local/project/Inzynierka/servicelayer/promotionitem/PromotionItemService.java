package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.PromotionItem;
import local.project.Inzynierka.persistence.repository.PromotionItemRepository;
import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStrategy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionItemService {

    private final PromotionItemRepository promotionItemRepository;
    private final PromotionItemTypeFetcher promotionItemTypeFetcher;

    public PromotionItemService(PromotionItemRepository promotionItemRepository, PromotionItemTypeFetcher promotionItemTypeFetcher) {
        this.promotionItemRepository = promotionItemRepository;
        this.promotionItemTypeFetcher = promotionItemTypeFetcher;
    }

    public void addPromotionItem(PromotionItemAddedEvent promotionItemAddedEvent) {

        promotionItemRepository.save(map(promotionItemAddedEvent));
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

    private PromotionItem map(PromotionItemAddedEvent promotionItemAddedEvent) {

        var promotionItemType = promotionItemTypeFetcher.fetch(promotionItemAddedEvent.getPromotionItemType().name());
        var numberOfPhotos = Optional.ofNullable(promotionItemAddedEvent.getNumberOfPhotos()).orElse(0);
        var validFrom = Optional.ofNullable(promotionItemAddedEvent.getStartTime()).orElse(Instant.now());

        return PromotionItem.builder()
                .company(Company.builder().id(promotionItemAddedEvent.getCompanyId()).build())
                .htmlContent(promotionItemAddedEvent.getHTMLContent())
                .name(promotionItemAddedEvent.getTitle())
                .nonHtmlContent(promotionItemAddedEvent.getNonHtmlContent())
                .numberOfPhotos(numberOfPhotos)
                .validFrom(Timestamp.from(validFrom))
                .wasSent(SendingStrategy.AT_CREATION.equals(promotionItemAddedEvent.getSendingStrategy()))
                .promotionItemType(promotionItemType)
                .build();
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
