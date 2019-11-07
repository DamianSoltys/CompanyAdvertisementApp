package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.PromotionItemType;

public interface PromotionItemTypesRepository extends ApplicationSmallRepository<PromotionItemType> {

    PromotionItemType findByType(String type);
}
