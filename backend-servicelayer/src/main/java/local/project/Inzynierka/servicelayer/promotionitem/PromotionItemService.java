package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.PromotionItem;
import local.project.Inzynierka.persistence.entity.PromotionItemDestination;
import local.project.Inzynierka.persistence.entity.PromotionItemPhoto;
import local.project.Inzynierka.persistence.repository.PromotionItemDestinationRepository;
import local.project.Inzynierka.persistence.repository.PromotionItemPhotosRepository;
import local.project.Inzynierka.persistence.repository.PromotionItemRepository;
import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.GetPromotionItemDto;
import local.project.Inzynierka.servicelayer.dto.promotionitem.PromotionItemSendingStatusDto;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStatus;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStrategy;
import local.project.Inzynierka.shared.ApplicationConstants;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PromotionItemService {

    private final PromotionItemRepository promotionItemRepository;
    private final PromotionItemTypeFetcher promotionItemTypeFetcher;
    private final PromotionItemDestinationRepository promotionItemDestinationRepository;
    private final PromotionItemPhotosRepository promotionItemPhotosRepository;

    public PromotionItemService(PromotionItemRepository promotionItemRepository, PromotionItemTypeFetcher promotionItemTypeFetcher, PromotionItemDestinationRepository promotionItemDestinationRepository, PromotionItemPhotosRepository promotionItemPhotosRepository) {
        this.promotionItemRepository = promotionItemRepository;
        this.promotionItemTypeFetcher = promotionItemTypeFetcher;
        this.promotionItemDestinationRepository = promotionItemDestinationRepository;
        this.promotionItemPhotosRepository = promotionItemPhotosRepository;
    }

    public PromotionItemSendingStatusDto addPromotionItem(PromotionItemAddedEvent promotionItemAddedEvent) {

        var promotionItem = promotionItemRepository.save(mapNewPromotionItem(promotionItemAddedEvent));
        promotionItemDestinationRepository.saveAll(mapDestinations(promotionItemAddedEvent.getDestinations(), promotionItem));
        List<PromotionItemPhoto> photos = getPromotionItemPhotos(promotionItemAddedEvent, promotionItem);
        var persistedPhotos = promotionItemPhotosRepository.saveAll(photos);

        List<PromotionItemSender> senders = buildSenders(promotionItemAddedEvent.getDestinations());

        promotionItemAddedEvent.setUUID(promotionItem.getPromotionItemUUID());
        var readyToSend = promotionItemAddedEvent.getNumberOfPhotos() == null;
        if (readyToSend) {
            commissionSendingPromotionItem(promotionItemAddedEvent, senders);
        }

        return PromotionItemSendingStatusDto.builder()
                .promotionItemPhotosUUIDsDto(StreamSupport.stream(persistedPhotos.spliterator(), false)
                                                     .map(PromotionItemPhoto::getPhotoUUID)
                                                     .collect(Collectors.toList()))
                .promotionItemUUID(promotionItem.getPromotionItemUUID())
                .sendingFinished(promotionItemAddedEvent.getNumberOfPhotos() == null)
                .build();
    }

    private void commissionSendingPromotionItem(Sendable sendable, List<PromotionItemSender> senders) {
        for (var sender : senders) {
            if (SendingStrategy.DELAYED.equals(sendable.getSendingStrategy())) {
                sender.schedule(sendable);
            } else if (SendingStrategy.AT_CREATION.equals(sendable.getSendingStrategy())) {
                sender.send(sendable);
            }
        }
    }

    private List<PromotionItemPhoto> getPromotionItemPhotos(PromotionItemAddedEvent promotionItemAddedEvent, PromotionItem promotionItem) {
        var photosUUIDNumber = Optional.ofNullable(promotionItemAddedEvent.getNumberOfPhotos()).orElse(0);
        List<PromotionItemPhoto> photos = new ArrayList<>();
        for (int i = 0; i < photosUUIDNumber; i++) {
            String photoUUID = UUID.randomUUID().toString();
            photos.add(PromotionItemPhoto.builder()
                               .photoUUID(photoUUID)
                               .promotionItem(promotionItem)
                               .wasAdded(false)
                               .build());
        }
        return photos;
    }

    private Collection<PromotionItemDestination> mapDestinations(Set<Destination> destinations, PromotionItem promotionItem) {
        return destinations.stream()
                .map(destination -> PromotionItemDestination.builder()
                        .destination(destination.name())
                        .promotionItem(promotionItem)
                        .build())
                .collect(Collectors.toList());
    }

    private PromotionItem mapNewPromotionItem(PromotionItemAddedEvent promotionItemAddedEvent) {

        var promotionItemType = promotionItemTypeFetcher.fetch(promotionItemAddedEvent.getPromotionItemType().name());
        var numberOfPhotos = Optional.ofNullable(promotionItemAddedEvent.getNumberOfPhotos()).orElse(0);
        var plannedSendingTime = promotionItemAddedEvent.getSendingStrategy().equals(SendingStrategy.DELAYED) ?
                Timestamp.from(promotionItemAddedEvent.getPlannedSendingTime()) : null;
        var promotionItemUUID = UUID.randomUUID().toString();
        var wasSent = numberOfPhotos == 0 &&
                SendingStrategy.AT_CREATION.equals(promotionItemAddedEvent.getSendingStrategy());
        var sendAt = wasSent ? Timestamp.from(Instant.now()) : null;
        return PromotionItem.builder()
                .company(Company.builder().id(promotionItemAddedEvent.getCompanyId()).build())
                .htmlContent(promotionItemAddedEvent.getHTMLContent())
                .name(promotionItemAddedEvent.getName())
                .emailTitle(promotionItemAddedEvent.getEmailTitle())
                .nonHtmlContent(promotionItemAddedEvent.getNonHtmlContent())
                .numberOfPhotos(numberOfPhotos)
                .plannedSendingAt(plannedSendingTime)
                .sendAt(sendAt)
                .wasSent(wasSent)
                .promotionItemType(promotionItemType)
                .promotionItemUUID(promotionItemUUID)
                .build();
    }

    private List<PromotionItemSender> buildSenders(Collection<Destination> destinations) {
        List<PromotionItemSender> senders = new ArrayList<>();

        if (destinations.contains(Destination.FB)) {
            var fbSender = new FacebookPromotionItemSender();
            senders.add(fbSender);
        }
        if (destinations.contains(Destination.TWITTER)) {
            var twitterSender = new TwitterPromotionItemSender();
            senders.add(twitterSender);
        }
        if (destinations.contains(Destination.NEWSLETTER)) {
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
        var plannedSendingAt = promotionItem.getPlannedSendingAt() ==
                null ? null : promotionItem.getPlannedSendingAt().toInstant().getEpochSecond();
        var sendAt = promotionItem.getSendAt() == null ? null : promotionItem.getSendAt().toInstant().getEpochSecond();

        return GetPromotionItemDto.builder()
                .addedTime(promotionItem.getCreatedDate().toInstant().getEpochSecond())
                .plannedSendingTime(plannedSendingAt)
                .sendTime(sendAt)
                .destinations(destinations.stream()
                                      .map(destination -> Destination.valueOf(destination.getDestination()))
                                      .collect(Collectors.toList()))
                .sendingStatus(sendingStatus)
                .name(promotionItem.getName())
                .promotionItemUUID(promotionItem.getPromotionItemUUID())
                .build();
    }

    private SendingStatus getSendingStatus(PromotionItem promotionItem) {
        SendingStatus sendingStatus;

        if (promotionItem.isWasSent()) {
            sendingStatus = SendingStatus.SENT;
        } else {
            if (promotionItem.getPlannedSendingAt() == null) {
                sendingStatus = SendingStatus.WAITING_FOR_ACTION;
            } else {
                sendingStatus = SendingStatus.DELAYED;
            }
        }
        return sendingStatus;
    }

    public Boolean finalizePromotionItemSending(String promotionItemUUID) {
        var promotionItem = promotionItemRepository.findByPromotionItemUUID(promotionItemUUID).get();

        if (!promotionItem.isWasSent()) {
            var numberOfPhotosAdded = promotionItemPhotosRepository.countAllByPromotionItemAndWasAddedTrue(promotionItem);
            var readyToSend = numberOfPhotosAdded.equals(promotionItem.getNumberOfPhotos());
            if (readyToSend) {
                var destinations = promotionItemDestinationRepository.findByPromotionItem(promotionItem)
                        .stream()
                        .map(destination -> Destination.valueOf(destination.getDestination()))
                        .collect(Collectors.toList());

                var persistedSendable = buildPersistedSendable(promotionItem, destinations);
                var senders = buildSenders(destinations);
                commissionSendingPromotionItem(persistedSendable, senders);
            }
            return readyToSend;
        }

        return true;
    }

    private PersistedSendable buildPersistedSendable(PromotionItem promotionItem, List<Destination> destinations) {

        var photosURLs = promotionItemPhotosRepository.findByPromotionItem(promotionItem)
                .stream()
                .map(PromotionItemPhoto::getPhotoUUID)
                .collect(Collectors.toList());
        var plannedSendingTime =
                promotionItem.getPlannedSendingAt() == null ? null : promotionItem.getPlannedSendingAt().toInstant();

        return PersistedSendable.builder()
                .appUrl(ApplicationConstants.CLIENT_URL)
                .companyId(promotionItem.getCompany().getId())
                .content(promotionItem.getNonHtmlContent())
                .htmlContent(promotionItem.getHtmlContent())
                .plannedSendingTime(plannedSendingTime)
                .emailTitle(promotionItem.getEmailTitle())
                .sendingStrategy(buildSendingStrategy(promotionItem))
                .name(promotionItem.getName())
                .UUID(promotionItem.getPromotionItemUUID())
                .destinations(destinations)
                .photoURLs(photosURLs)
                .build();
    }

    private SendingStrategy buildSendingStrategy(PromotionItem promotionItem) {
        SendingStrategy strategy;
        //ONLY TWO SENDING STRATEGIES BECAUSE THERE IS NO WAY DISCERN
        // AT_WILL FROM AT_CREATION WITHOUT ADDITIONAL COLUMN IN PROMOTION_ITEM
        if (promotionItem.getPlannedSendingAt() == null) {
            strategy = SendingStrategy.AT_CREATION;
        } else {
            strategy = SendingStrategy.DELAYED;
        }

        return strategy;
    }

    @Async
    @EventListener
    public void handlePhotoAdding(PromotionItemPhotoAddedEvent promotionItemPhotoAddedEvent) {
        var photo = promotionItemPhotosRepository.findByPhotoUUID(promotionItemPhotoAddedEvent.getPhotoUUID());
        photo.setWasAdded(true);
        promotionItemPhotosRepository.save(photo);
    }

    @Async
    @EventListener
    public void markPromotionItemSent(PromotionItemSentEvent promotionItemSentEvent) {
        var promotionItem = promotionItemRepository.findByPromotionItemUUID(promotionItemSentEvent.getPromotionItemUUID()).get();
        promotionItem.setWasSent(true);
        promotionItem.setSendAt(Timestamp.from(Instant.now()));
        promotionItemRepository.save(promotionItem);
    }
}
