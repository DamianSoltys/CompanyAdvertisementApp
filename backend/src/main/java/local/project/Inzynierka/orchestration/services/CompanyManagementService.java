package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.AddressRepository;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.CategoryRepository;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.shared.AuthenticationFacade;
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
    public void registerCompany(Company company, List<Branch> branches) {
        User user = userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());
        NaturalPerson naturalPerson = user.getNaturalPerson();

        Category soughtCategory = categoryRepository.findByName(company.getCategory().getName());
        if (soughtCategory == null) {
            soughtCategory = company.getCategory();
            soughtCategory.setName(company.getCategory().getName());
            soughtCategory.setId((short) 0);
        }

        Category category = categoryRepository.save(soughtCategory);

        Voivoideship voivoideship = voivodeshipRepository.findByName(company.getAddress().getVoivodeship_id().getName());

        Address address = Address.builder()
                .apartmentNo(company.getAddress().getApartmentNo())
                .buildingNo(company.getAddress().getBuildingNo())
                .id(0L)
                .city(company.getAddress().getCity())
                .voivodeship_id(voivoideship)
                .street(company.getAddress().getStreet())
                .build();

        address = addressRepository.save(address);

        Company companyInCreation = Company.builder()
                .address(address)
                .category(category)
                .description(company.getDescription())
                .hasBranch(company.isHasBranch())
                .id(0L)
                .name(company.getName())
                .NIP(company.getNIP())
                .registerer(naturalPerson)
                .logoPath(company.getLogoPath())
                .REGON(company.getREGON())
                .build();

        Company createdCompany = companyRepository.save(companyInCreation);

        if (!companyInCreation.isHasBranch()) {
            return;
        }

        for (int i = 0; i < branches.size(); i++) {
            Branch branch = branches.get(i);
            branch.setCompany(createdCompany);
            branch.setRegisterer(naturalPerson);
            branch.setId(0L);
            Voivoideship branchVoivodeship = voivodeshipRepository.findByName(branch.getAddress().getVoivodeship_id().getName());
            branch.getAddress().setVoivodeship_id(branchVoivodeship);
            Address branchAddress = addressRepository.save(branch.getAddress());
            branch.setAddress(branchAddress);

            branches.set(i, branch);
        }

        branchRepository.saveAll(branches);
    }

    public Company getThroughBranch(Long id) {
        return companyRepository.getByCompanyId(id);
    }
}
