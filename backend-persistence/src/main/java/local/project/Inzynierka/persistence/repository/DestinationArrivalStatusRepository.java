package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.DestinationArrival;
import local.project.Inzynierka.persistence.entity.PromotionItem;
import local.project.Inzynierka.persistence.entity.PromotionItemDestination;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationArrivalStatusRepository extends ApplicationBigRepository<DestinationArrival> {

    DestinationArrival findByPromotionItemDestination(PromotionItemDestination promotionItemDestination);
    List<DestinationArrival> findByPromotionItemDestination_PromotionItem(PromotionItem promotionItem);
}
