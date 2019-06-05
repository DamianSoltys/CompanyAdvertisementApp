package local.project.Inzynierka.web.security;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.shared.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyManagementPermissionService {

    private final AuthenticationFacade authenticationFacade;

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyManagementPermissionService(AuthenticationFacade authenticationFacade, UserRepository userRepository, CompanyRepository companyRepository) {
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public boolean hasRegisteringAuthority(){
        User user = userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());

        System.out.println(user);

        if(user == null) {
            return false;
        }

        return user.getNaturalPerson() != null;
    }

    public boolean hasManagingAuthority(Long companyId) {
        User user = userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());
        if( user == null ) {
            return false;
        }
        NaturalPerson naturalPerson = user.getNaturalPerson();
        if( naturalPerson == null) {
            return false;
        }

        Company company = companyRepository.findById(companyId).orElse(new Company());
        List<Company> userCompanies = companyRepository.findByRegisterer(naturalPerson);

        if( userCompanies == null || userCompanies.isEmpty()) {
            return false;
        }

        return userCompanies.stream().anyMatch(c -> c.equals(company));

    }
}
