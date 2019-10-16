package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.servicelayer.dto.AddCompanyDto;
import local.project.Inzynierka.servicelayer.dto.BranchBuildDto;
import local.project.Inzynierka.servicelayer.dto.CompanyBuildDto;
import local.project.Inzynierka.servicelayer.dto.CompanyInfoDto;
import local.project.Inzynierka.servicelayer.dto.CompanyRelatedDeletedEntities;
import local.project.Inzynierka.servicelayer.dto.UpdateCompanyInfoDto;
import local.project.Inzynierka.servicelayer.dto.mapper.AddressMapper;
import local.project.Inzynierka.servicelayer.dto.mapper.CompanyExtractor;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyManagementService {

    private final CompanyRepository companyRepository;

    private final CompanyPersistenceService companyPersistenceService;

    private final BranchPersistenceService branchPersistenceService;

    public CompanyManagementService(CompanyRepository companyRepository, CompanyPersistenceService companyPersistenceService, BranchPersistenceService branchPersistenceService) {
        this.companyRepository = companyRepository;
        this.companyPersistenceService = companyPersistenceService;
        this.branchPersistenceService = branchPersistenceService;
    }

    @Transactional
    public CompanyBuildDto registerCompany(AddCompanyDto addCompanyDto, UserAccount registerer) {

        var companyExtractor = new CompanyExtractor(addCompanyDto);
        List<Branch> branches = companyExtractor.getBranches();
        Company company = companyExtractor.getCompany();

        Company createdCompany = this.companyPersistenceService.getPersistedCompany(company, registerer);

        if (!createdCompany.hasBranch()) {
            return mapToCompanyBuildDto(createdCompany);
        }

        this.branchPersistenceService.buildAllCompanyBranches(branches, createdCompany);

        Iterable<Branch> persistedBranches = this.branchPersistenceService.saveAll(branches);
        var branchCollection = new ArrayList<Branch>();
        persistedBranches.forEach(branchCollection::add);

        return mapToCompanyBuildDto(createdCompany, branchCollection);
    }

    private CompanyBuildDto mapToCompanyBuildDto(Company createdCompany) {
        return CompanyBuildDto.builder()
                .id(createdCompany.getId())
                .logoKey(getLogoKey(createdCompany.getLogoPath()))
                .logoFilePath(createdCompany.getLogoPath())
                .build();
    }

    private CompanyBuildDto mapToCompanyBuildDto(Company createdCompany, List<Branch> branches) {
        return CompanyBuildDto.builder()
                .branchBuildDTOs(
                        branches.stream()
                                .map(branch -> BranchBuildDto.builder()
                                        .id(branch.getId())
                                        .logoFilePath(branch.getPhotoPath())
                                        .logoKey(getLogoKey(branch.getPhotoPath()))
                                        .build())
                                .collect(Collectors.toList()))
                .id(createdCompany.getId())
                .logoFilePath(createdCompany.getLogoPath())
                .logoKey(getLogoKey(createdCompany.getLogoPath()))
                .build();
    }

    private String getLogoKey(String logoPath) {
        List<String> backslashSplitPath = Arrays.asList(logoPath.split(File.separator));
        String logoFileName = backslashSplitPath.get(backslashSplitPath.size() - 1);
        return logoFileName.substring(0, logoFileName.length() - 4);
    }

    public boolean companyExists(Long id) {
        return this.companyRepository.getByCompanyId(id) != null;
    }

    public Optional<CompanyInfoDto> getCompanyInfo(Long id) {

        Optional<Company> optionalCompany = this.companyPersistenceService.getPersistedCompany(id);

        return optionalCompany.map(this::buildCompanyInfoDto);
    }

    //TODO move to some better place
    private String getRegistererFullname(Company company) {
        return String.format("%s %s", company.getRegisterer().getFirstName(), company.getRegisterer().getLastName());
    }

    @Transactional
    public Optional<CompanyInfoDto> updateCompanyInfo(Long id, UpdateCompanyInfoDto updateCompanyInfoDto) {

        Optional<Company> optionalCompany = this.companyPersistenceService.getPersistedCompany(id);

        return optionalCompany
                .map(company -> updateCompany(updateCompanyInfoDto, company))
                .map(this::buildCompanyInfoDto);
    }

    private Company updateCompany(UpdateCompanyInfoDto updateCompanyInfoDto, Company company) {
        AddressMapper addressMapper = new AddressMapper();
        String companyWebsiteLink = updateCompanyInfoDto.getCompanyWebsiteLink();
        company.setCompanyWebsiteLink(
                companyWebsiteLink == null ? company.getCompanyWebsiteLink() : companyWebsiteLink);
        company.setAddress(
                updateCompanyInfoDto.getAddress() == null ? company.getAddress() :
                        this.companyPersistenceService.getPersistedAddress(addressMapper.map(updateCompanyInfoDto.getAddress())));
        company.setCategory(updateCompanyInfoDto.getCategory() == null ? company.getCategory() :
                                    this.companyPersistenceService.getPersistedCategory(new Category(updateCompanyInfoDto.getCategory())));
        company.setDescription(updateCompanyInfoDto.getDescription() == null ? company.getDescription() :
                                       updateCompanyInfoDto.getDescription());
        company.setName(updateCompanyInfoDto.getCompanyName() == null ? company.getName() :
                                updateCompanyInfoDto.getCompanyName());


        return this.companyRepository.save(company);
    }

    private CompanyInfoDto buildCompanyInfoDto(Company company) {

        AddressMapper addressMapper = new AddressMapper();
        return CompanyInfoDto.builder()
                .category(company.getCategory().getName())
                .companyId(company.getId())
                .companyName(company.getName())
                .REGON(company.getREGON())
                .NIP(company.getNIP())
                .address(addressMapper.map(company.getAddress()))
                .description(company.getDescription())
                .registererFullname(this.getRegistererFullname(company))
                .companyWebsiteUrl(company.getCompanyWebsiteLink())
                .branchesIDs(company.hasBranch() ? this.branchPersistenceService.getCompanyBranchesIds(company.getId()) : Collections.emptyList())
                .build();
    }

    @Transactional
    public Optional<CompanyRelatedDeletedEntities> deleteCompany(Long id) {

        Optional<Company> companyOptional = this.companyPersistenceService.getPersistedCompany(id);

        CompanyRelatedDeletedEntities companyRelatedDeletedEntities =
                companyOptional.map(this::buildCompanyDeletedIdentities).orElseThrow(IllegalStateException::new);

        this.companyRepository.deleteById(id);
        this.companyPersistenceService.deleteAddresses(companyRelatedDeletedEntities.getAddressesIds());


        return Optional.of(companyRelatedDeletedEntities);
    }

    private CompanyRelatedDeletedEntities buildCompanyDeletedIdentities(Company company) {

        List<Long> branches = this.branchPersistenceService.getCompanyBranchesIds(company.getId());
        List<Long> commentsIds = company.hasBranch() ? this.companyPersistenceService.getCommentsIdsRelatedToCompanyBranches(branches) : Collections.emptyList();
        List<List<Long>> favouriteBranchesIds = company.hasBranch() ? this.companyPersistenceService.getFavouriteBranchesRelatedToCompany(branches) : Collections.emptyList();
        List<Long> ratingsIds = company.hasBranch() ? this.companyPersistenceService.getRatingsIdsRelatedToCompanyBranches(branches) : Collections.emptyList();

        return CompanyRelatedDeletedEntities.builder()
                .companyId(company.getId())
                .commentsIds(commentsIds)
                .branchesIds(branches)
                .socialProfileUrls(this.companyPersistenceService.getSocialProfileUrlsRelatedToCompany(company.getId()))
                .promotionItemsIds(this.companyPersistenceService.getPromotionItemsRelatedToCompany(company.getId()))
                .newsletterSubscriptions(this.companyPersistenceService.getNewsletterSubscriptionsRelatedToCompany(company.getId()))
                .ratingsIds(ratingsIds)
                .favouriteBranchesIds(favouriteBranchesIds)
                .addressesIds(this.companyPersistenceService.getAddressesIdsRelatedToCompany(company.getId()))
                .build();
    }

}
