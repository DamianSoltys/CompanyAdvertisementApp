package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.DestinationArrival;
import local.project.Inzynierka.persistence.entity.PromotionItem;
import local.project.Inzynierka.persistence.entity.PromotionItemDestination;
import local.project.Inzynierka.persistence.entity.PromotionItemPhoto;
import local.project.Inzynierka.persistence.repository.DestinationArrivalStatusRepository;
import local.project.Inzynierka.persistence.repository.PromotionItemDestinationRepository;
import local.project.Inzynierka.persistence.repository.PromotionItemPhotosRepository;
import local.project.Inzynierka.persistence.repository.PromotionItemRepository;
import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.DestinationSendingStatus;
import local.project.Inzynierka.servicelayer.dto.promotionitem.GetPromotionItemDto;
import local.project.Inzynierka.servicelayer.dto.promotionitem.PromotionItemAddingStatusDto;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStatus;
import local.project.Inzynierka.persistence.constants.SendingStrategy;
import local.project.Inzynierka.servicelayer.promotionitem.error.InvalidSendingStrategyException;
import local.project.Inzynierka.servicelayer.promotionitem.event.SendingEvent;
import local.project.Inzynierka.shared.ApplicationConstants;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;

@Service
public class PromotionItemService {

    private final PromotionItemRepository promotionItemRepository;
    private final PromotionItemTypeFetcher promotionItemTypeFetcher;
    private final PromotionItemDestinationRepository promotionItemDestinationRepository;
    private final PromotionItemPhotosRepository promotionItemPhotosRepository;
    private final DestinationArrivalStatusRepository destinationArrivalStatusRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public PromotionItemService(PromotionItemRepository promotionItemRepository, PromotionItemTypeFetcher promotionItemTypeFetcher, PromotionItemDestinationRepository promotionItemDestinationRepository, PromotionItemPhotosRepository promotionItemPhotosRepository, DestinationArrivalStatusRepository destinationArrivalStatusRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.promotionItemRepository = promotionItemRepository;
        this.promotionItemTypeFetcher = promotionItemTypeFetcher;
        this.promotionItemDestinationRepository = promotionItemDestinationRepository;
        this.promotionItemPhotosRepository = promotionItemPhotosRepository;
        this.destinationArrivalStatusRepository = destinationArrivalStatusRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public PromotionItemAddingStatusDto addPromotionItem(PromotionItemAddedEvent promotionItemAddedEvent) {

        var promotionItem = promotionItemRepository.save(mapNewPromotionItem(promotionItemAddedEvent));
        promotionItemDestinationRepository.saveAll(mapDestinations(promotionItemAddedEvent.getDestinations(), promotionItem));
        List<PromotionItemPhoto> photos = getPromotionItemPhotos(promotionItemAddedEvent, promotionItem);
        var persistedPhotos = promotionItemPhotosRepository.saveAll(photos);

        List<PromotionItemSender> senders = buildSenders(promotionItemAddedEvent.getDestinations());

        promotionItemAddedEvent.setUUID(promotionItem.getPromotionItemUUID());
        if (Boolean.TRUE.equals(promotionItem.getAddingCompleted()) && !promotionItem.hasAtWillSendingStrategy()) {
            commissionSendingPromotionItem(promotionItemAddedEvent, senders);
        }
        if (promotionItem.hasAtWillSendingStrategy()) {
            publishWaitingForActionStatuses(promotionItem);
        }

        return PromotionItemAddingStatusDto.builder()
                .promotionItemPhotosUUIDsDto(StreamSupport.stream(persistedPhotos.spliterator(), false)
                                                     .map(PromotionItemPhoto::getPhotoUUID)
                                                     .collect(Collectors.toList()))
                .promotionItemUUID(promotionItem.getPromotionItemUUID())
                .addingFinished(promotionItem.getAddingCompleted())
                .build();
    }

    private void commissionSendingPromotionItem(Sendable sendable, List<PromotionItemSender> senders) {
        for (var sender : senders) {
            if (SendingStrategy.DELAYED.equals(sendable.getSendingStrategy())) {
                sender.schedule(sendable);
            } else if (SendingStrategy.AT_CREATION.equals(sendable.getSendingStrategy())) {
                sender.send(sendable);
            } else if (SendingStrategy.AT_WILL.equals(sendable.getSendingStrategy())) {
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
        var addingCompleted = numberOfPhotos == 0;

        return PromotionItem.builder()
                .company(Company.builder().id(promotionItemAddedEvent.getCompanyId()).build())
                .htmlContent(promotionItemAddedEvent.getHTMLContent())
                .name(promotionItemAddedEvent.getName())
                .addingCompleted(addingCompleted)
                .emailTitle(promotionItemAddedEvent.getEmailTitle())
                .nonHtmlContent(promotionItemAddedEvent.getNonHtmlContent())
                .numberOfPhotos(numberOfPhotos)
                .sendingStrategy(promotionItemAddedEvent.getSendingStrategy().toString())
                .plannedSendingAt(plannedSendingTime)
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

        var sendingStatuses = getSendingStatus(promotionItem);

        return GetPromotionItemDto.builder()
                .addedTime(promotionItem.getCreatedDate().toInstant().getEpochSecond())
                .sendingStatus(sendingStatuses)
                .name(promotionItem.getName())
                .promotionItemUUID(promotionItem.getPromotionItemUUID())
                .build();
    }

    private List<DestinationSendingStatus> getSendingStatus(PromotionItem promotionItem) {

        var arrivals = getLastlyCreatedArrivalDestinations(destinationArrivalStatusRepository.findByPromotionItemDestination_PromotionItem(promotionItem));
        return arrivals.stream()
                .map(arrival -> DestinationSendingStatus.builder()
                        .destination(Destination.fromDestination(arrival.getPromotionItemDestination().getDestination()))
                        .detail(arrival.getDetail())
                        .failedAt(SendingStatus.fromSendingStatus(arrival.getStatus()).equals(SendingStatus.FAILED) ?
                                          arrival.getCreatedDate().toInstant().getEpochSecond() :
                                          null)
                        .sendAt(SendingStatus.fromSendingStatus(arrival.getStatus()).equals(SendingStatus.SENT) ?
                                        arrival.getCreatedDate().toInstant().getEpochSecond() :
                                        null)
                        .plannedSendingAt(SendingStatus.fromSendingStatus(arrival.getStatus()).equals(SendingStatus.DELAYED) ?
                                                  arrival.getPromotionItemDestination()
                                                          .getPromotionItem()
                                                          .getPlannedSendingAt()
                                                          .toInstant()
                                                          .getEpochSecond() :
                                                  null)
                        .sendingStatus(SendingStatus.fromSendingStatus(arrival.getStatus()))
                        .build())
                .collect(Collectors.toList());
    }

    public Collection<DestinationArrival> getLastlyCreatedArrivalDestinations(List<DestinationArrival> arrivals) {
        return arrivals.stream()
                .collect(groupingBy(DestinationArrival::getPromotionItemDestination))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Function.identity(), e -> e.getValue()
                        .stream().max(Comparator.comparing(DestinationArrival::getCreatedDate)).get()))
                .values();
    }

    public Boolean finalizePromotionItemAdding(String promotionItemUUID) {
        var promotionItem = promotionItemRepository.findByPromotionItemUUID(promotionItemUUID).get();

        if (Boolean.FALSE.equals(promotionItem.getAddingCompleted())) {
            var numberOfPhotosAdded = promotionItemPhotosRepository.countAllByPromotionItemAndWasAddedTrue(promotionItem);
            var readyToSend = numberOfPhotosAdded.equals(promotionItem.getNumberOfPhotos());
            if (readyToSend) {
                promotionItem.setAddingCompleted(true);
                promotionItemRepository.save(promotionItem);

                if (!SendingStrategy.AT_WILL.toString().equals(promotionItem.getSendingStrategy())) {
                    startSending(promotionItem);
                } else {
                    publishWaitingForActionStatuses(promotionItem);
                }

            }
            return readyToSend;
        }

        return true;
    }

    private void publishWaitingForActionStatuses(PromotionItem promotionItem) {
        promotionItemDestinationRepository.findByPromotionItem(promotionItem).stream()
                .map(destination -> SendingEvent.builder()
                        .promotionItemUUUID(promotionItem.getPromotionItemUUID())
                        .sendingStatus(SendingStatus.WAITING_FOR_ACTION)
                        .destination(Destination.fromDestination(destination.getDestination()))
                        .build())
                .forEach(applicationEventPublisher::publishEvent);
    }

    private PersistedSendable buildPersistedSendable(PromotionItem promotionItem, List<Destination> destinations) {

        var photosURLs = promotionItemPhotosRepository.findByPromotionItem(promotionItem)
                .stream()
                .map(PromotionItemPhoto::getPhotoUUID)
                .collect(Collectors.toList());
        var plannedSendingTime = promotionItem.getPlannedSendingAt() == null ?
                null : promotionItem.getPlannedSendingAt().toInstant();

        return PersistedSendable.builder()
                .appUrl(ApplicationConstants.CLIENT_URL)
                .companyId(promotionItem.getCompany().getId())
                .content(promotionItem.getNonHtmlContent())
                .htmlContent(promotionItem.getHtmlContent())
                .plannedSendingTime(plannedSendingTime)
                .emailTitle(promotionItem.getEmailTitle())
                .sendingStrategy(SendingStrategy.fromSendingStrategy(promotionItem.getSendingStrategy()))
                .name(promotionItem.getName())
                .UUID(promotionItem.getPromotionItemUUID())
                .destinations(destinations)
                .photoURLs(photosURLs)
                .build();
    }

    @Async
    @EventListener
    public void handlePhotoAdding(PromotionItemPhotoAddedEvent promotionItemPhotoAddedEvent) {
        var photo = promotionItemPhotosRepository.findByPhotoUUID(promotionItemPhotoAddedEvent.getPhotoUUID());
        photo.setWasAdded(true);
        promotionItemPhotosRepository.save(photo);
    }

    public Boolean finalizePromotionItemSending(String promotionItemUUID) {
        var promotionItem = promotionItemRepository.findByPromotionItemUUID(promotionItemUUID).get();

        if (Boolean.TRUE.equals(promotionItem.getAddingCompleted())) {

            if (SendingStrategy.AT_WILL.toString().equals(promotionItem.getSendingStrategy())) {
                startSending(promotionItem);
                return true;
            }
            throw new InvalidSendingStrategyException();
        }

        return false;
    }

    private void startSending(PromotionItem promotionItem) {
        var destinations = promotionItemDestinationRepository.findByPromotionItem(promotionItem)
                .stream()
                .map(destination -> Destination.valueOf(destination.getDestination()))
                .collect(Collectors.toList());


        var persistedSendable = buildPersistedSendable(promotionItem, destinations);
        var senders = buildSenders(destinations);
        commissionSendingPromotionItem(persistedSendable, senders);
    }
}
