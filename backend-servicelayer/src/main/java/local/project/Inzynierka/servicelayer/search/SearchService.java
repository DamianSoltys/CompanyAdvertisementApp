package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.servicelayer.dto.mapper.AddressMapper;
import local.project.Inzynierka.shared.utils.EntityName;
import local.project.Inzynierka.shared.utils.LogoFilePathCreator;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SearchService {

    private final EntityManager entityManager;
    private final AddressMapper addressMapper;
    private final BranchRepository branchRepository;

    public SearchService(EntityManager entityManager, AddressMapper addressMapper, BranchRepository branchRepository) {
        this.entityManager = entityManager;
        this.addressMapper = addressMapper;
        this.branchRepository = branchRepository;
    }

    @Transactional
    public Page<Object> searchForEntities(String term, Pageable pageable) {
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

        QueryBuilder branchBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Branch.class).get();
        QueryBuilder companyBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Company.class).get();

        org.apache.lucene.search.Query booleanQuery = buildFinalQuery(term, branchBuilder, companyBuilder);

        Query jpaQuery =
                fullTextEntityManager.createFullTextQuery(booleanQuery, Branch.class, Company.class);

        List result = jpaQuery.getResultList();

        List<Object> res = extractEntities(result);

        return new PageImpl<>(res, pageable, res.size());
    }

    private org.apache.lucene.search.Query buildFinalQuery(String term, QueryBuilder branchBuilder, QueryBuilder companyBuilder) {
        org.apache.lucene.search.Query branchQuery = branchBuilder
                .keyword()
                .fuzzy()
                .onFields("name", "address.city", "address.street")
                .matching(term)
                .createQuery();

        org.apache.lucene.search.Query companyQuery = companyBuilder
                .keyword()
                .fuzzy()
                .onFields("name", "category.name", "address.city", "address.street")
                .matching(term)
                .createQuery();

        return branchBuilder.bool()
                .should(companyQuery)
                .should(branchQuery)
                .createQuery();
    }

    private List<Object> extractEntities(List result) {
        List<Object> res = new ArrayList<>();
        result.stream().forEach(entity -> {
            mapIfBranch(res, entity);
            mapIfCompany(res, entity);
        });
        return res;
    }

    private void mapIfCompany(List<Object> res, Object entity) {
        if (entity instanceof Company) {
            Company company = (Company) entity;
            res.add(buildSearchableCompany(company));

        }
    }

    private SearchableCompanyDto buildSearchableCompany(Company company) {
        return SearchableCompanyDto.builder()
                .hasLogoAdded(company.isHasLogoAdded())
                .logoPath(company.getLogoPath())
                .logoKey(LogoFilePathCreator.getLogoKey(company.getLogoPath()))
                .id(company.getId())
                .category(company.getCategory().getName())
                .name(company.getName())
                .address(addressMapper.map(company.getAddress()))
                .build();
    }

    private void mapIfBranch(List<Object> res, Object entity) {
        if (entity instanceof Branch) {
            Branch branch = (Branch) entity;
            res.add(buildSearchableBranch(branch));
        }
    }

    private SearchableBranchDto buildSearchableBranch(Branch branch) {
        return SearchableBranchDto.builder()
                .category(branch.getCompany().getCategory().getName())
                .hasLogoAdded(branch.isHasLogoAdded())
                .logoPath(branch.getPhotoPath())
                .logoKey(LogoFilePathCreator.getLogoKey(branch.getPhotoPath()))
                .companyId(branch.getCompany().getId())
                .id(branch.getId())
                .name(branch.getName())
                .address(addressMapper.map(branch.getAddress()))
                .build();
    }

    @Transactional
    public Page<Object> searchForEntities(SearchSpecification searchSpecification, Pageable pageable) {

        List<Object> result = null;
        if (searchSpecification.getType() != null) {
            if (EntityName.BRANCH.getEntityName().equals(searchSpecification.getType())) {
                result = Collections.singletonList(branchRepository.searchForBranch(searchSpecification.getCity()));
            }
            if (EntityName.COMPANY.getEntityName().equals(searchSpecification.getType())) {

            }
        } else {

        }

        return new PageImpl<>(result, pageable, result.size());
    }
}
