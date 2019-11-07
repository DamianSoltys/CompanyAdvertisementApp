package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.PromotionItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionItemRepository extends ApplicationBigRepository<PromotionItem> {

    List<PromotionItem> findByCompany(Company company);
}
