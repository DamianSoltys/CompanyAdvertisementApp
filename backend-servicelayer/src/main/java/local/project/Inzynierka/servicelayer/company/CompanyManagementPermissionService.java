package local.project.Inzynierka.servicelayer.company;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyManagementPermissionService {

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    public CompanyManagementPermissionService(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public boolean hasRegisteringAuthority(UserAccount userAccount) {

        return Optional.ofNullable(
                userRepository.getByAddressEmail(userAccount.getEmail()))
                .isPresent();
    }

    public boolean hasManagingAuthority(Long companyId, UserAccount userAccount) {
        User user = userRepository.getByAddressEmail(userAccount.getEmail());

        if (user == null) {
            return false;
        }
        NaturalPerson naturalPerson = user.getNaturalPerson();
        if (naturalPerson == null) {
            return false;
        }

        Company requestedCompany = companyRepository.findById(companyId).orElse(new Company());
        List<Company> userCompanies = companyRepository.findByRegisterer(naturalPerson);

        return userCompanies.stream().anyMatch(usersCompany -> usersCompany.equals(requestedCompany));
    }

    public boolean hasManagingAuthority(String companyUUID, UserAccount userAccount) {
        User user = userRepository.getByAddressEmail(userAccount.getEmail());

        if (user == null) {
            return false;
        }
        NaturalPerson naturalPerson = user.getNaturalPerson();
        if (naturalPerson == null) {
            return false;
        }

        Company requestedCompany = companyRepository.findByCompanyUUID(companyUUID).orElse(new Company());
        List<Company> userCompanies = companyRepository.findByRegisterer(naturalPerson);

        return userCompanies.stream().anyMatch(usersCompany -> usersCompany.equals(requestedCompany));
    }
}
