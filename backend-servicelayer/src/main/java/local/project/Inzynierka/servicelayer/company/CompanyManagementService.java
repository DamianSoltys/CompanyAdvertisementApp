package local.project.Inzynierka.servicelayer.company;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.servicelayer.company.event.AllBranchesHasBeenDeletedEvent;
import local.project.Inzynierka.servicelayer.company.event.CompanyLogoAddedEvent;
import local.project.Inzynierka.servicelayer.dto.branch.AddBranchDto;
import local.project.Inzynierka.servicelayer.dto.branch.BranchBuildDto;
import local.project.Inzynierka.servicelayer.dto.branch.PersistedBranchDto;
import local.project.Inzynierka.servicelayer.dto.company.AddCompanyDto;
import local.project.Inzynierka.servicelayer.dto.company.CompanyBuildDto;
import local.project.Inzynierka.servicelayer.dto.company.CompanyInfoDto;
import local.project.Inzynierka.servicelayer.dto.company.CompanyRelatedDeletedEntities;
import local.project.Inzynierka.servicelayer.dto.company.UpdateCompanyInfoDto;
import local.project.Inzynierka.servicelayer.dto.mapper.AddressMapper;
import local.project.Inzynierka.servicelayer.dto.mapper.CompanyExtractor;
import local.project.Inzynierka.servicelayer.dto.social.SocialProfileConnectionDto;
import local.project.Inzynierka.servicelayer.social.SocialMediaConnectionService;
import local.project.Inzynierka.shared.UserAccount;
import local.project.Inzynierka.shared.utils.FilePathCreator;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyManagementService {

    private final CompanyRepository companyRepository;

    private final CompanyPersistenceService companyPersistenceService;

    private final BranchPersistenceService branchPersistenceService;

    private final SocialMediaConnectionService socialMediaConnectionService;

    public CompanyManagementService(CompanyRepository companyRepository, CompanyPersistenceService companyPersistenceService, BranchPersistenceService branchPersistenceService, SocialMediaConnectionService socialMediaConnectionService) {
        this.companyRepository = companyRepository;
        this.companyPersistenceService = companyPersistenceService;
        this.branchPersistenceService = branchPersistenceService;
        this.socialMediaConnectionService = socialMediaConnectionService;
    }

    private static CompanyBuildDto mapToCompanyBuildDto(Company createdCompany) {
        return CompanyBuildDto.builder()
                .id(createdCompany.getId())
                .logoKey(FilePathCreator.getFileKey(createdCompany.getLogoPath()))
                .getLogoURL(createdCompany.getLogoPath())
                .putLogoURL(FilePathCreator.getPutLogoURL(createdCompany.getLogoPath()))
                .build();
    }

    private static CompanyBuildDto mapToCompanyBuildDto(Company createdCompany, List<Branch> branches) {
        return CompanyBuildDto.builder()
                .branchBuildDTOs(
                        branches.stream()
                                .map(branch -> BranchBuildDto.builder()
                                        .id(branch.getId())
                                        .getLogoURL(branch.getPhotoPath())
                                        .putLogoURL(FilePathCreator.getPutLogoURL(branch.getPhotoPath()))
                                        .logoKey(FilePathCreator.getFileKey(branch.getPhotoPath()))
                                        .build())
                                .collect(Collectors.toList()))
                .id(createdCompany.getId())
                .getLogoURL(createdCompany.getLogoPath())
                .putLogoURL(FilePathCreator.getPutLogoURL(createdCompany.getLogoPath()))
                .logoKey(FilePathCreator.getFileKey(createdCompany.getLogoPath()))
                .build();
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

    public boolean companyExists(Long id) {
        return this.companyRepository.findById(id).isPresent();
    }

    public Optional<CompanyInfoDto> getCompanyInfo(Long id, UserAccount userAccount) {

        Optional<Company> optionalCompany = this.companyPersistenceService.getPersistedCompany(id);

        return optionalCompany.map(company -> buildCompanyInfoDto(company, userAccount));
    }

    //TODO move to some better place
    private String getRegistererFullname(Company company) {
        return String.format("%s %s", company.getRegisterer().getFirstName(), company.getRegisterer().getLastName());
    }

    @Transactional
    public Optional<CompanyInfoDto> updateCompanyInfo(Long id, UpdateCompanyInfoDto updateCompanyInfoDto, UserAccount userAccount) {

        Optional<Company> optionalCompany = this.companyPersistenceService.getPersistedCompany(id);

        return optionalCompany
                .map(company -> updateCompany(updateCompanyInfoDto, company))
                .map(company -> buildCompanyInfoDto(company, userAccount));
    }

    private Company updateCompany(UpdateCompanyInfoDto updateCompanyInfoDto, Company company) {
        AddressMapper addressMapper = new AddressMapper();
        String companyWebsiteLink = updateCompanyInfoDto.getCompanyWebsiteUrl();
        company.setCompanyWebsiteLink(
                companyWebsiteLink == null ? company.getCompanyWebsiteLink() : companyWebsiteLink);
        company.setAddress(
                updateCompanyInfoDto.getAddress() == null ? company.getAddress() :
                        this.companyPersistenceService.getPersistedAddress(addressMapper.map(updateCompanyInfoDto.getAddress())));
        company.setCategory(updateCompanyInfoDto.getCategory() == null ? company.getCategory() :
                                    this.companyPersistenceService.getPersistedCategory(new Category(updateCompanyInfoDto.getCategory())));
        company.setDescription(updateCompanyInfoDto.getDescription() == null ? company.getDescription() :
                                       updateCompanyInfoDto.getDescription());
        company.setName(updateCompanyInfoDto.getName() == null ? company.getName() :
                                updateCompanyInfoDto.getName());

        company.setNIP(updateCompanyInfoDto.getNip() == null ? company.getNIP() : updateCompanyInfoDto.getNip());
        company.setREGON(
                updateCompanyInfoDto.getRegon() == null ? company.getREGON() : updateCompanyInfoDto.getRegon());


        return this.companyRepository.save(company);
    }

    private CompanyInfoDto buildCompanyInfoDto(Company company, UserAccount userAccount) {

        boolean isAllowedToSeeConnectionStatus = Optional.ofNullable(userAccount)
                .map(account -> userAccount.isNaturalPersonRegistered() &&
                        company.getRegisterer().getId().equals(userAccount.personId()))
                .orElse(false);
        List<SocialProfileConnectionDto> socialMediaConnections = isAllowedToSeeConnectionStatus ?
                socialMediaConnectionService.getSocialProfileConnections(company.getId()) :
                Collections.emptyList();
        AddressMapper addressMapper = new AddressMapper();

        return CompanyInfoDto.builder()
                .hasLogoAdded(company.isHasLogoAdded())
                .logoKey(FilePathCreator.getFileKey(company.getLogoPath()))
                .getLogoURL(company.getLogoPath())
                .putLogoURL(FilePathCreator.getPutLogoURL(company.getLogoPath()))
                .logoKey(FilePathCreator.getFileKey(company.getLogoPath()))
                .category(company.getCategory().getName())
                .companyId(company.getId())
                .socialProfileConnectionDtos(socialMediaConnections)
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

    @Transactional
    public Optional<List<PersistedBranchDto>> addBranch(Long id, List<AddBranchDto> addBranchDto, UserAccount userAccount) {

        updateHasBranchFlagForCompany(id);
        return this.branchPersistenceService.saveBranch(addBranchDto, id, userAccount.personId());
    }

    private void updateHasBranchFlagForCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(IllegalStateException::new);
        company.setHasBranch(true);
        companyRepository.save(company);
    }

    @Async
    @EventListener
    public void setCompanyHasLogoFlag(CompanyLogoAddedEvent companyLogoAddedEvent) {
        Company company = companyRepository.findByCompanyUUID(companyLogoAddedEvent.getCompanyUUID())
                .orElseThrow(IllegalStateException::new);
        company.setHasLogoAdded(true);
        companyRepository.save(company);
    }

    @Async
    @EventListener
    public void markCompanyBranchless(AllBranchesHasBeenDeletedEvent allBranchesHasBeenDeletedEvent) {

        Company company = companyRepository.findById(allBranchesHasBeenDeletedEvent.getCompanyId())
                .orElseThrow(IllegalStateException::new);
        company.setHasBranch(false);
        companyRepository.save(company);
    }
}
