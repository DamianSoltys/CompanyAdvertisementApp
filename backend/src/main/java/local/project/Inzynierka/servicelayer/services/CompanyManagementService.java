package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.AddressRepository;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.CategoryRepository;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.servicelayer.dto.AddCompanyDto;
import local.project.Inzynierka.shared.AuthenticationFacade;
import local.project.Inzynierka.web.mapper.CompanyExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyManagementService {

    private final CompanyRepository companyRepository;

    private final BranchRepository branchRepository;

    private final UserRepository userRepository;

    private final AuthenticationFacade authenticationFacade;

    private final CategoryRepository categoryRepository;

    private final VoivodeshipRepository voivodeshipRepository;

    private final AddressRepository addressRepository;

    public CompanyManagementService(CompanyRepository companyRepository, BranchRepository branchRepository, UserRepository userRepository, AuthenticationFacade authenticationFacade, CategoryRepository categoryRepository, VoivodeshipRepository voivodeshipRepository, AddressRepository addressRepository) {
        this.companyRepository = companyRepository;
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
        this.categoryRepository = categoryRepository;
        this.voivodeshipRepository = voivodeshipRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public void registerCompany(AddCompanyDto addCompanyDto) {

        CompanyExtractor companyExtractor = new CompanyExtractor(addCompanyDto);
        List<Branch> branches = companyExtractor.getBranches();
        Company company = companyExtractor.getCompany();

        Company createdCompany = this.getPersistedCompany(company);

        if (!createdCompany.isHasBranch()) {
            return;
        }

        this.buildAllCompanyBranches(branches, createdCompany);

        this.branchRepository.saveAll(branches);
    }

    private Company getPersistedCompany(Company company) {

        User user = this.userRepository.getByAddressEmail(this.authenticationFacade.getAuthentication().getName());

        return this.companyRepository.save(Company.builder()
                .address(this.getPersistedAddress(company))
                .category(this.getPersistedCategory(company))
                .description(company.getDescription())
                .hasBranch(company.isHasBranch())
                .id(0L)
                .name(company.getName())
                .NIP(company.getNIP())
                .registerer(user.getNaturalPerson())
                .logoPath(company.getLogoPath())
                .REGON(company.getREGON())
                .build());
    }

    private void buildAllCompanyBranches(List<Branch> branches, Company createdCompany) {
        branches.forEach(branch -> {
            branch.setCompany(createdCompany);
            branch.setRegisterer(createdCompany.getRegisterer());
            branch.setId(0L);
            Voivoideship branchVoivodeship = this.voivodeshipRepository.findByName(branch.getAddress().getVoivodeship_id().getName());
            branch.getAddress().setVoivodeship_id(branchVoivodeship);
            Address branchAddress = this.addressRepository.save(branch.getAddress());
            branch.setAddress(branchAddress);
        });
    }

    private Address getPersistedAddress(Company company) {
        Voivoideship voivoideship = this.voivodeshipRepository.findByName(company.getAddress().getVoivodeship_id().getName());

        Address address = Address.builder()
                .apartmentNo(company.getAddress().getApartmentNo())
                .buildingNo(company.getAddress().getBuildingNo())
                .id(0L)
                .city(company.getAddress().getCity())
                .voivodeship_id(voivoideship)
                .street(company.getAddress().getStreet())
                .build();

        return this.addressRepository.save(address);
    }

    private Category getPersistedCategory(Company company) {
        Category soughtCategory = this.categoryRepository.findByName(company.getCategory().getName());
        if (soughtCategory == null) {
            soughtCategory = company.getCategory();
            soughtCategory.setName(company.getCategory().getName());
            soughtCategory.setId((short) 0);
            this.categoryRepository.save(soughtCategory);
        }

        return soughtCategory;
    }

    public boolean companyExists(Long id) {
        return this.companyRepository.getByCompanyId(id) != null;
    }
}
