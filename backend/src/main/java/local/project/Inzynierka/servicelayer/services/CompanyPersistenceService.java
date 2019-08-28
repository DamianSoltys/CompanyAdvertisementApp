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

import java.util.Optional;

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

    Company getPersistedCompany(Company company) {

        User user = this.userRepository.getByAddressEmail(this.authenticationFacade.getAuthentication().getName());

        return this.companyRepository.save(Company.builder()
                                                   .address(this.getPersistedAddress(company.getAddress()))
                                                   .category(this.getPersistedCategory(company.getCategory()))
                                                   .description(company.getDescription())
                                                   .hasBranch(company.hasBranch())
                                                   .name(company.getName())
                                                   .NIP(company.getNIP())
                                                   .registerer(user.getNaturalPerson())
                                                   .logoPath(company.getLogoPath())
                                                   .REGON(company.getREGON())
                                                   .build());
    }

    Optional<Company> getPersistedCompany(Long id) {
        return this.companyRepository.findById(id);
    }

    private Address getPersistedAddress(Address address) {
        Voivoideship voivoideship = this.voivodeshipRepository.findByName(address.getVoivodeship_id().getName());

        Address addressToPersist = Address.builder()
                .apartmentNo(address.getApartmentNo())
                .buildingNo(address.getBuildingNo())
                .city(address.getCity())
                .voivodeship_id(voivoideship)
                .street(address.getStreet())
                .build();

        return this.addressRepository.save(addressToPersist);
    }

    private Category getPersistedCategory(Category category) {
        Category soughtCategory = this.categoryRepository.findByName(category.getName());
        if (soughtCategory == null) {
            soughtCategory = this.categoryRepository.save(new Category(category.getName()));
        }

        return soughtCategory;
    }


}
