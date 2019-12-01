package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.servicelayer.dto.mapper.AddressMapper;
import local.project.Inzynierka.servicelayer.dto.search.SearchResultDto;
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
    public SearchResultDto searchForEntities(String term) {
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

        QueryBuilder branchBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Branch.class).get();
        QueryBuilder companyBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Company.class).get();

        org.apache.lucene.search.Query booleanQuery = buildFinalQuery(term, branchBuilder, companyBuilder);

        Query jpaQuery =
                fullTextEntityManager.createFullTextQuery(booleanQuery, Branch.class, Company.class);

        List result = jpaQuery.getResultList();

        return extractEntities(result);
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

    private SearchResultDto extractEntities(List result) {
        List<Object> res = new ArrayList<>();
        var companiesNumber = new Object() {int value = 0;};
        var branchesNumber = new Object(){int value = 0;};

        result.stream().forEach(entity -> {
            if (entity instanceof Branch) {
                branchesNumber.value += 1;
                Branch branch = (Branch) entity;
                res.add(buildSearchableBranch(branch));
            }
            if (entity instanceof Company) {
                companiesNumber.value += 1;
                Company company = (Company) entity;
                res.add(buildSearchableCompany(company));

            }
        });
        return SearchResultDto.builder()
                .result(res)
                .branchesNumber(branchesNumber.value)
                .companiesNumber(companiesNumber.value)
                .build();
    }

    private SearchableCompanyDto buildSearchableCompany(Company company) {
        return SearchableCompanyDto.builder()
                .hasLogoAdded(company.isHasLogoAdded())
                .getLogoURL(company.getLogoPath())
                .putLogoURL(FilePathCreator.getPutLogoURL(company.getLogoPath()))
                .logoKey(FilePathCreator.getFileKey(company.getLogoPath()))
                .id(company.getId())
                .category(company.getCategory().getName())
                .name(company.getName())
                .address(addressMapper.map(company.getAddress()))
                .build();
    }

    private SearchableBranchDto buildSearchableBranch(Branch branch) {
        return SearchableBranchDto.builder()
                .category(branch.getCompany().getCategory().getName())
                .hasLogoAdded(branch.isHasLogoAdded())
                .getLogoURL(branch.getPhotoPath())
                .putLogoURL(FilePathCreator.getPutLogoURL(branch.getPhotoPath()))
                .logoKey(FilePathCreator.getFileKey(branch.getPhotoPath()))
                .companyId(branch.getCompany().getId())
                .id(branch.getId())
                .name(branch.getName())
                .address(addressMapper.map(branch.getAddress()))
                .build();
    }

    @Transactional
    public SearchResultDto searchForEntities(List<SearchSpecification> specifications) {

        var companiesNumber = new Object() {int value = 0;};
        var branchesNumber = new Object(){int value = 0;};

        List<Object> result = specifications.stream().map(specification -> {
            if (specification instanceof BranchSearchSpecification) {
                var branches = getBranchesAccordingToBranchSearchSpecification(specification);
                branchesNumber.value += branches.size();
                return branches;
            } else if (specification instanceof CompanySearchSpecification) {
                var companies = getCompaniesAccordingToCompanySearchSpecification(specification);
                companiesNumber.value += companies.size();
                return companies;
            }
            throw new IllegalStateException(String.format("Invalid specification: %s", specification.getClass().getName()));
        }).flatMap(List::stream).collect(Collectors.toList());



        return SearchResultDto.builder()
                .result(result)
                .branchesNumber(branchesNumber.value)
                .companiesNumber(companiesNumber.value)
                .build();
    }

    private List<Object> getCompaniesAccordingToCompanySearchSpecification(SearchSpecification searchSpecification) {
        return companyRepository.findAll((CompanySearchSpecification) searchSpecification)
                .stream()
                .map(this::buildSearchableCompany)
                .collect(Collectors.toList());
    }

    private List<Object> getBranchesAccordingToBranchSearchSpecification(SearchSpecification searchSpecification) {
        return branchRepository.findAll((BranchSearchSpecification) searchSpecification)
                .stream()
                .map(this::buildSearchableBranch)
                .collect(Collectors.toList());
    }
}
