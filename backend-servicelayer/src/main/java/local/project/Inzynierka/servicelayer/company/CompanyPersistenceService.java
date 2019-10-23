package local.project.Inzynierka.servicelayer.company;

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
import local.project.Inzynierka.servicelayer.errors.InvalidVoivodeshipException;
import local.project.Inzynierka.shared.UserAccount;
import local.project.Inzynierka.shared.utils.EntityName;
import local.project.Inzynierka.shared.utils.LogoFilePathCreator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyPersistenceService {


    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private final AddressRepository addressRepository;

    private final VoivodeshipRepository voivodeshipRepository;

    private final CategoryRepository categoryRepository;

    public CompanyPersistenceService(UserRepository userRepository, CompanyRepository companyRepository, AddressRepository addressRepository, VoivodeshipRepository voivodeshipRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.addressRepository = addressRepository;
        this.voivodeshipRepository = voivodeshipRepository;
        this.categoryRepository = categoryRepository;
    }

    Company getPersistedCompany(Company company, UserAccount userAccount) {

        User user = this.userRepository.getByAddressEmail(userAccount.getEmail());
        String entityUUID = UUID.randomUUID().toString();

        return this.companyRepository.save(Company.builder()
                                                   .address(this.getPersistedAddress(company.getAddress()))
                                                   .category(this.getPersistedCategory(company.getCategory()))
                                                   .description(company.getDescription())
                                                   .hasBranch(company.hasBranch())
                                                   .name(company.getName())
                                                   .NIP(company.getNIP())
                                                   .registerer(user.getNaturalPerson())
                                                   .companyUUID(entityUUID)
                                                   .logoPath(LogoFilePathCreator.buildEntityLogoURL(entityUUID, EntityName.COMPANY))
                                                   .REGON(company.getREGON())
                                                   .build());
    }

    Optional<Company> getPersistedCompany(Long id) {
        return this.companyRepository.findById(id);
    }

    public Address getPersistedAddress(Address address) {
        Voivoideship voivoideship = this.voivodeshipRepository.findByName(address.getVoivodeship_id().getName()).orElseThrow(InvalidVoivodeshipException::new);

        Address addressToPersist = Address.builder()
                .apartmentNo(address.getApartmentNo())
                .buildingNo(address.getBuildingNo())
                .city(address.getCity())
                .voivodeship_id(voivoideship)
                .street(address.getStreet())
                .build();

        return this.addressRepository.save(addressToPersist);
    }

    public Category getPersistedCategory(Category category) {
        Category soughtCategory = this.categoryRepository.findByName(category.getName());
        if (soughtCategory == null) {
            soughtCategory = this.categoryRepository.save(new Category(category.getName()));
        }

        return soughtCategory;
    }

    public List<Long> getAddressesIdsRelatedToCompany(Long id) {
        return this.companyRepository.getAddressesRelatedToCompany(id);
    }

    public List<Long> getCommentsIdsRelatedToCompanyBranches(List<Long> ids) {
        return this.companyRepository.getCommentsRelatedToBranches(ids);
    }

    public List<Long> getRatingsIdsRelatedToCompanyBranches(List<Long> ids) {
        return this.companyRepository.getRatingsRelatedToBranches(ids);
    }

    public List<List<Long>> getFavouriteBranchesRelatedToCompany(List<Long> ids) {
        return this.companyRepository.getFavouriteBranchesRelatedToCompany(ids);
    }

    public List<Long> getNewsletterSubscriptionsRelatedToCompany(Long id) {
        return this.companyRepository.getNewsletterSubscriptionsRelatedToCompany(id);
    }

    public List<Long> getPromotionItemsRelatedToCompany(Long id) {
        return this.companyRepository.getPromotionItemsRelatedToCompany(id);
    }

    public List<String> getSocialProfileUrlsRelatedToCompany(Long id) {
        return this.companyRepository.getSocialProfileUrlRelatedToCompany(id);
    }

    public void deleteAddresses(List<Long> addresses) {
        this.addressRepository.deleteAllById(addresses);
    }
}
