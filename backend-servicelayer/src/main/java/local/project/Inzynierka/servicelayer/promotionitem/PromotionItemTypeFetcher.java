package local.project.Inzynierka.servicelayer.promotionitem;

import local.project.Inzynierka.persistence.entity.PromotionItemType;
import local.project.Inzynierka.persistence.repository.PromotionItemTypesRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class PromotionItemTypeFetcher {

    private final PromotionItemTypesRepository promotionItemTypesRepository;

    public PromotionItemTypeFetcher(PromotionItemTypesRepository promotionItemTypesRepository) {
        this.promotionItemTypesRepository = promotionItemTypesRepository;
    }

    @Cacheable("PItypes")
    public PromotionItemType fetch(String promotionItemType) {

        return promotionItemTypesRepository.findByType(promotionItemType);
    }
}
