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

        var promotionItem = promotionItemRepository.save(map(promotionItemAddedEvent));
        promotionItemDestinationRepository.saveAll(mapDestinations(promotionItemAddedEvent.getDestinations(), promotionItem));
        List<PromotionItemPhoto> photos = getPromotionItemPhotos(promotionItemAddedEvent, promotionItem);
        var persistedPhotos = promotionItemPhotosRepository.saveAll(photos);

        List<PromotionItemSender> senders = buildSenders(promotionItemAddedEvent.getDestinations());

        if (promotionItemAddedEvent.getNumberOfPhotos() == null) {
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

    private PromotionItem map(PromotionItemAddedEvent promotionItemAddedEvent) {

        var promotionItemType = promotionItemTypeFetcher.fetch(promotionItemAddedEvent.getPromotionItemType().name());
        var numberOfPhotos = Optional.ofNullable(promotionItemAddedEvent.getNumberOfPhotos()).orElse(0);
        var validFrom = promotionItemAddedEvent.getSendingStrategy().equals(SendingStrategy.AT_WILL) ? null :
                Timestamp.from(Optional.ofNullable(promotionItemAddedEvent.getStartTime())
                                       .orElse(Instant.now()));
        var promotionItemUUID = UUID.randomUUID().toString();
        return PromotionItem.builder()
                .company(Company.builder().id(promotionItemAddedEvent.getCompanyId()).build())
                .htmlContent(promotionItemAddedEvent.getHTMLContent())
                .name(promotionItemAddedEvent.getTitle())
                .nonHtmlContent(promotionItemAddedEvent.getNonHtmlContent())
                .numberOfPhotos(numberOfPhotos)
                .validFrom(validFrom)
                .wasSent(numberOfPhotos == 0 &&
                                 SendingStrategy.AT_CREATION.equals(promotionItemAddedEvent.getSendingStrategy()))
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
        return GetPromotionItemDto.builder()
                .dateAdded(Instant.from(promotionItem.getCreatedDate().toInstant()))
                .destinations(destinations.stream()
                                      .map(destination -> Destination.valueOf(destination.getDestination()))
                                      .collect(Collectors.toList()))
                .sendingStatus(sendingStatus)
                .promotionItemUUID(promotionItem.getPromotionItemUUID())
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

                promotionItem.setWasSent(true);
                promotionItemRepository.save(promotionItem);

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

        return PersistedSendable.builder()
                .appUrl(ApplicationConstants.CLIENT_URL)
                .companyId(promotionItem.getCompany().getId())
                .content(promotionItem.getNonHtmlContent())
                .htmlContent(promotionItem.getHtmlContent())
                .startTime(promotionItem.getValidFrom().toInstant())
                .title(promotionItem.getName())
                .UUID(promotionItem.getPromotionItemUUID())
                .sendingStrategy(buildSendingStrategy(promotionItem))
                .destinations(destinations)
                .photoURLs(photosURLs)
                .build();
    }

    private SendingStrategy buildSendingStrategy(PromotionItem promotionItem) {
        SendingStrategy sendingStrategy;
        if (promotionItem.getValidFrom() == null) {
            sendingStrategy = SendingStrategy.AT_WILL;
        } else {
            if (promotionItem.getValidFrom().after(Timestamp.from(Instant.now().plusSeconds(600)))) {
                sendingStrategy = SendingStrategy.DELAYED;
            } else {
                sendingStrategy = SendingStrategy.AT_CREATION;
            }
        }
        return sendingStrategy;
    }

    @Async
    @EventListener
    public void handlePhotoAdding(PromotionItemPhotoAddedEvent promotionItemPhotoAddedEvent) {
        var photo = promotionItemPhotosRepository.findByPhotoUUID(promotionItemPhotoAddedEvent.getPhotoUUID());
        photo.setWasAdded(true);
        promotionItemPhotosRepository.save(photo);
    }
}
