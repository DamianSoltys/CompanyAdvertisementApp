package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.servicelayer.dto.mapper.AddressMapper;
import local.project.Inzynierka.shared.utils.FilePathCreator;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final EntityManager entityManager;
    private final AddressMapper addressMapper;
    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;

    public SearchService(EntityManager entityManager, AddressMapper addressMapper, BranchRepository branchRepository, CompanyRepository companyRepository) {
        this.entityManager = entityManager;
        this.addressMapper = addressMapper;
        this.branchRepository = branchRepository;
        this.companyRepository = companyRepository;
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

        return toPage(res, pageable);
    }

    private Page<Object> toPage(List<Object> list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
                list.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<Object> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
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
                .logoKey(FilePathCreator.getFileKey(company.getLogoPath()))
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
                .logoKey(FilePathCreator.getFileKey(branch.getPhotoPath()))
                .companyId(branch.getCompany().getId())
                .id(branch.getId())
                .name(branch.getName())
                .address(addressMapper.map(branch.getAddress()))
                .build();
    }

    @Transactional
    public Page<Object> searchForEntities(List<SearchSpecification> specifications, Pageable pageable) {

        List<Object> result = specifications.stream().map(specification -> {
            if (specification instanceof BranchSearchSpecification) {
                return getBranchesAccordingToBranchSearchSpecification(specification, pageable);
            } else if (specification instanceof CompanySearchSpecification) {
                return getCompaniesAccordingToCompanySearchSpecification(specification, pageable);
            }
            throw new IllegalStateException(String.format("Invalid specification: %s", specification.getClass().getName()));
        }).flatMap(List::stream).collect(Collectors.toList());

        return toPage(result, pageable);
    }

    private List<Object> getCompaniesAccordingToCompanySearchSpecification(SearchSpecification searchSpecification, Pageable pageable) {
        return companyRepository.findAll((CompanySearchSpecification) searchSpecification, pageable)
                .stream()
                .map(this::buildSearchableCompany)
                .collect(Collectors.toList());
    }

    private List<Object> getBranchesAccordingToBranchSearchSpecification(SearchSpecification searchSpecification, Pageable pageable) {
        return branchRepository.findAll((BranchSearchSpecification) searchSpecification, pageable)
                .stream()
                .map(this::buildSearchableBranch)
                .collect(Collectors.toList());
    }
}
