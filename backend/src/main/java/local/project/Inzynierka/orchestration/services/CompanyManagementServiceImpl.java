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
import local.project.Inzynierka.shared.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CompanyManagementServiceImpl implements CompanyManagementService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VoivodeshipRepository voivodeshipRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional
    public void registerCompany(Company company, List<Branch> branches) {
        User user = userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());
        NaturalPerson naturalPerson = user.getNaturalPerson();

        Timestamp now = DateUtils.getNowTimestamp();

        Category soughtCategory = categoryRepository.findByName(company.getCategory().getName());
        if (soughtCategory == null) {
            soughtCategory = company.getCategory();
            soughtCategory.setCreatedAt(now);
            soughtCategory.setName(company.getCategory().getName());
            soughtCategory.setId((short) 0);
        }

        soughtCategory.setModifiedAt(now);
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
                .createdAt(now)
                .modifiedAt(now)
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
            branch.setCreatedAt(now);
            branch.setModifiedAt(now);
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

    @Override
    public Company getThroughBranch(Long id) {
        return companyRepository.getByCompanyId(id);
    }
}
