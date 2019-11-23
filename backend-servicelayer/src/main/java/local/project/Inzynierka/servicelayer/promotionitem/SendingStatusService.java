package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.persistence.entity.DestinationArrival;
import local.project.Inzynierka.persistence.entity.PromotionItemDestination;
import local.project.Inzynierka.persistence.repository.DestinationArrivalStatusRepository;
import local.project.Inzynierka.persistence.repository.PromotionItemDestinationRepository;
import local.project.Inzynierka.servicelayer.promotionitem.event.SendingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendingStatusService {

    private final DestinationArrivalStatusRepository destinationArrivalStatusRepository;
    private final PromotionItemDestinationRepository promotionItemDestinationRepository;

    public SendingStatusService(DestinationArrivalStatusRepository destinationArrivalStatusRepository, PromotionItemDestinationRepository promotionItemDestinationRepository) {
        this.destinationArrivalStatusRepository = destinationArrivalStatusRepository;
        this.promotionItemDestinationRepository = promotionItemDestinationRepository;
    }

    @EventListener
    @Async
    public void handleSendingEvent(SendingEvent sendingEvent) {

        DestinationArrival destinationArrival = DestinationArrival.builder()
                .detail(sendingEvent.getDetail())
                .promotionItemDestination(getDestination(sendingEvent))
                .status(sendingEvent.getSendingStatus().toString())
                .build();
        destinationArrivalStatusRepository.save(destinationArrival);
    }

    private PromotionItemDestination getDestination(SendingEvent sendingEvent) {
        return promotionItemDestinationRepository.findByPromotionItem_PromotionItemUUIDAndAndDestination(sendingEvent.getPromotionItemUUUID(), sendingEvent.getDestination().toString());
    }
}
