package local.project.Inzynierka.persistence.repository;


import local.project.Inzynierka.persistence.entity.PromotionItem;
import local.project.Inzynierka.persistence.entity.PromotionItemDestination;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionItemDestinationRepository extends ApplicationBigRepository<PromotionItemDestination> {
    List<PromotionItemDestination> findByPromotionItem(PromotionItem promotionItem);

    PromotionItemDestination findByPromotionItem_PromotionItemUUIDAndAndDestination(String promotionItemUUID,
                                                                                    String destination);
}
