package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.PromotionItem;
import local.project.Inzynierka.persistence.entity.PromotionItemPhoto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionItemPhotosRepository extends ApplicationBigRepository<PromotionItemPhoto> {

    Integer countAllByPromotionItemAndWasAddedTrue(PromotionItem promotionItem);

    List<PromotionItemPhoto> findByPromotionItem(PromotionItem promotionItem);

    PromotionItemPhoto findByPhotoUUID(String photoUUID);
}
