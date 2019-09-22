package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.servicelayer.dto.UserInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPersistenceService {

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    public UserPersistenceService(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public UserInfoDto getUserInfo(Long id) {

        User authenticatedUser = this.userRepository.findById(id).orElseThrow(IllegalStateException::new);
        UserInfoDto userInfoDto = new UserInfoDto();
        if (authenticatedUser.hasRegisteredNaturalPerson()) {
            userInfoDto.setNaturalPersonID(authenticatedUser.getNaturalPerson().getId());
            userInfoDto.setCompaniesIDs(this.companyRepository
                                                .findByRegisterer(authenticatedUser.getNaturalPerson())
                                                .stream().map(Company::getId)
                                                .collect(Collectors.toList()));
        }
        userInfoDto.setUserID(authenticatedUser.getId());
        userInfoDto.setEmailAddress(authenticatedUser.getEmailAddressEntity().getEmail());
        userInfoDto.setLoginName(authenticatedUser.getName());

        return userInfoDto;
    }

    public List<Long> getCommentsOfUser(Long id) {
        return this.userRepository.getCommentsOfUser(id);
    }

    public List<Long> getRatingsOfUser(Long id) {
        return this.userRepository.getRatingsOfUser(id);
    }

    public List<Long> getSubscriptionsOfUser(Long id) {
        return this.userRepository.getSubscriptionsOfUser(id);
    }
}
