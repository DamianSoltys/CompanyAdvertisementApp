package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.AddressRepository;
import local.project.Inzynierka.persistence.repository.CategoryRepository;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.shared.AuthenticationFacade;
import org.springframework.stereotype.Service;

@Service
public class CompanyPersistenceService {

    private final UserRepository userRepository;

    private final AuthenticationFacade authenticationFacade;

    private final CompanyRepository companyRepository;

    private final AddressRepository addressRepository;

    private final VoivodeshipRepository voivodeshipRepository;

    private final CategoryRepository categoryRepository;

    public CompanyPersistenceService(UserRepository userRepository, AuthenticationFacade authenticationFacade, CompanyRepository companyRepository, AddressRepository addressRepository, VoivodeshipRepository voivodeshipRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
        this.companyRepository = companyRepository;
        this.addressRepository = addressRepository;
        this.voivodeshipRepository = voivodeshipRepository;
        this.categoryRepository = categoryRepository;
    }

    public Company getPersistedCompany(Company company) {

        User user = this.userRepository.getByAddressEmail(this.authenticationFacade.getAuthentication().getName());

        return this.companyRepository.save(Company.builder()
                                                   .address(this.getPersistedAddress(company))
                                                   .category(this.getPersistedCategory(company))
                                                   .description(company.getDescription())
                                                   .hasBranch(company.hasBranch())
                                                   .id(0L)
                                                   .name(company.getName())
                                                   .NIP(company.getNIP())
                                                   .registerer(user.getNaturalPerson())
                                                   .logoPath(company.getLogoPath())
                                                   .REGON(company.getREGON())
                                                   .build());
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


}
