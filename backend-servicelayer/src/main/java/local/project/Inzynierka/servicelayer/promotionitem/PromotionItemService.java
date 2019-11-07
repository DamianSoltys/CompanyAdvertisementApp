package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.PromotionItem;
import local.project.Inzynierka.persistence.entity.PromotionItemDestination;
import local.project.Inzynierka.persistence.repository.PromotionItemDestinationRepository;
import local.project.Inzynierka.persistence.repository.PromotionItemRepository;
import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.GetPromotionItemDto;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStatus;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStrategy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PromotionItemService {

    private final PromotionItemRepository promotionItemRepository;
    private final PromotionItemTypeFetcher promotionItemTypeFetcher;
    private final PromotionItemDestinationRepository promotionItemDestinationRepository;

    public PromotionItemService(PromotionItemRepository promotionItemRepository, PromotionItemTypeFetcher promotionItemTypeFetcher, PromotionItemDestinationRepository promotionItemDestinationRepository) {
        this.promotionItemRepository = promotionItemRepository;
        this.promotionItemTypeFetcher = promotionItemTypeFetcher;
        this.promotionItemDestinationRepository = promotionItemDestinationRepository;
    }

    public void addPromotionItem(PromotionItemAddedEvent promotionItemAddedEvent) {

        var promotionItem = promotionItemRepository.save(map(promotionItemAddedEvent));
        promotionItemDestinationRepository.saveAll(mapDestinations(promotionItemAddedEvent.getDestinations(), promotionItem));

        List<PromotionItemSender> senders = buildSenders(promotionItemAddedEvent);

        for (var sender : senders) {
            if (SendingStrategy.DELAYED.equals(promotionItemAddedEvent.getSendingStrategy())) {
                sender.schedule(promotionItemAddedEvent);
            } else if (SendingStrategy.AT_CREATION.equals(promotionItemAddedEvent.getSendingStrategy())) {
                sender.send(promotionItemAddedEvent);
            }
        }
    }

    private Collection<PromotionItemDestination> mapDestinations(Set<Destination> destinations, PromotionItem promotionItem) {
        return destinations.stream()
                .map(destination -> PromotionItemDestination.builder()
                        .destination(destination.name())
                        .promotionItem(promotionItem)
                        .build())
                .collect(Collectors.toList());
    }

    private PromotionItem map(PromotionItemAddedEvent promotionItemAddedEvent) {

        var promotionItemType = promotionItemTypeFetcher.fetch(promotionItemAddedEvent.getPromotionItemType().name());
        var numberOfPhotos = Optional.ofNullable(promotionItemAddedEvent.getNumberOfPhotos()).orElse(0);
        var validFrom = promotionItemAddedEvent.getSendingStrategy().equals(SendingStrategy.AT_WILL) ? null :
                Timestamp.from(Optional.ofNullable(promotionItemAddedEvent.getStartTime())
                                       .orElse(Instant.now()));

        return PromotionItem.builder()
                .company(Company.builder().id(promotionItemAddedEvent.getCompanyId()).build())
                .htmlContent(promotionItemAddedEvent.getHTMLContent())
                .name(promotionItemAddedEvent.getTitle())
                .nonHtmlContent(promotionItemAddedEvent.getNonHtmlContent())
                .numberOfPhotos(numberOfPhotos)
                .validFrom(validFrom)
                .wasSent(SendingStrategy.AT_CREATION.equals(promotionItemAddedEvent.getSendingStrategy()))
                .promotionItemType(promotionItemType)
                .build();
    }

    private List<PromotionItemSender> buildSenders(PromotionItemAddedEvent promotionItemAddedEvent) {
        List<PromotionItemSender> senders = new ArrayList<>();

        if (promotionItemAddedEvent.getDestinations().contains(Destination.FB)) {
            var fbSender = new FacebookPromotionItemSender();
            senders.add(fbSender);
        }
        if (promotionItemAddedEvent.getDestinations().contains(Destination.TWITTER)) {
            var twitterSender = new TwitterPromotionItemSender();
            senders.add(twitterSender);
        }
        if (promotionItemAddedEvent.getDestinations().contains(Destination.NEWSLETTER)) {
            var newsletterSender = new NewsletterPromotionItemSender();
            senders.add(newsletterSender);
        }
        return senders;
    }

    public List<GetPromotionItemDto> getPromotionItems(Long companyId) {

        var promotionItems = promotionItemRepository.findByCompany(Company.builder().id(companyId).build());
        return promotionItems.stream().map(this::mapGetDto).collect(Collectors.toList());
    }

    private GetPromotionItemDto mapGetDto(PromotionItem promotionItem) {

        var destinations = promotionItemDestinationRepository.findByPromotionItem(promotionItem);
        var sendingStatus = getSendingStatus(promotionItem);
        return GetPromotionItemDto.builder()
                .dateAdded(Instant.from(promotionItem.getCreatedDate().toInstant()))
                .destinations(destinations.stream()
                                      .map(destination -> Destination.valueOf(destination.getDestination()))
                                      .collect(Collectors.toList()))
                .sendingStatus(sendingStatus)
                .promotionItemId(promotionItem.getId())
                .build();
    }

    private SendingStatus getSendingStatus(PromotionItem promotionItem) {
        SendingStatus sendingStatus;

        if (promotionItem.isWasSent()) {
            sendingStatus = SendingStatus.SENT;
        } else {
            if (promotionItem.getValidFrom() == null) {
                sendingStatus = SendingStatus.WAITING_FOR_ACTION;
            } else {
                sendingStatus = SendingStatus.DELAYED;

            }
        }
        return sendingStatus;
    }
}
