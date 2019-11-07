package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.projections.CompanySendMailInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends ApplicationBigRepository<Company>, JpaSpecificationExecutor<Company> {

    List<Company> findByRegisterer(NaturalPerson registerer);

    @Query(value = "select a.id from branches b\n" +
            "    left join addresses a on b.address_id = a.id\n" +
            "where b.company_id = ?1\n" +
            "union\n" +
            "select c.address_id from companies c where c.id = ?1", nativeQuery = true)
    List<Long> getAddressesRelatedToCompany(Long id);

    @Query(value = "select com.id from comments com where com.branch_id in (?1)", nativeQuery = true)
    List<Long> getCommentsRelatedToBranches(List<Long> ids);

    @Query(value = "select rat.rating_id from ratings rat where rat.branch_id in( ?1)", nativeQuery = true)
    List<Long> getRatingsRelatedToBranches(List<Long> ids);

    @Query(value = "select fav.branch_id, fav.user_id from favourite_branches fav where branch_id in (?1)", nativeQuery = true)
    List<List<Long>> getFavouriteBranchesRelatedToCompany(List<Long> id);

    @Query(value = "select ns.id from newsletter_subscriptions ns where company_id = ?1", nativeQuery = true)
    List<Long> getNewsletterSubscriptionsRelatedToCompany(Long id);

    @Query(value = "select pi.promotion_item_id from promotion_items pi where pi.promoting_company_id = ?1", nativeQuery = true)
    List<Long> getPromotionItemsRelatedToCompany(Long id);

    @Query(value = "select sp.social_profile_url from social_profiles sp where sp.company_id = ?1", nativeQuery = true)
    List<String> getSocialProfileUrlRelatedToCompany(Long id);

    Optional<Company> findByCompanyUUID(String companyUUID);

    Optional<CompanySendMailInfo> getSendEmailInfoById(Long companyId);
}
