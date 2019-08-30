package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.servicelayer.dto.AddCompanyDto;
import local.project.Inzynierka.servicelayer.dto.CompanyInfoDto;
import local.project.Inzynierka.servicelayer.dto.UpdateCompanyInfoDto;
import local.project.Inzynierka.servicelayer.dto.mapper.AddressMapper;
import local.project.Inzynierka.servicelayer.dto.mapper.CompanyExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public void registerCompany(AddCompanyDto addCompanyDto) {

        CompanyExtractor companyExtractor = new CompanyExtractor(addCompanyDto);
        List<Branch> branches = companyExtractor.getBranches();
        Company company = companyExtractor.getCompany();

        Company createdCompany = this.companyPersistenceService.getPersistedCompany(company);

        if (!createdCompany.hasBranch()) {
            return;
        }

        this.branchPersistenceService.buildAllCompanyBranches(branches, createdCompany);

        this.branchPersistenceService.saveAll(branches);
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
                .address(addressMapper.map(company.getAddress()))
                .description(company.getDescription())
                .registererFullname(this.getRegistererFullname(company))
                .companyWebsiteUrl(company.getCompanyWebsiteLink())
                .branchesIDs(company.hasBranch() ? this.branchPersistenceService.getCompanyBranchesIds(company.getId()) : Collections.emptyList())
                .build();
    }
}
