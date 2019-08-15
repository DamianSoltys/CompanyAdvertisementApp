package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.entity.VerificationToken;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.AddressRepository;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.NaturalPersonRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.persistence.repository.VerificationTokenRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.servicelayer.dto.AuthenticatedUserInfoDto;
import local.project.Inzynierka.servicelayer.dto.UpdateUserDto;
import local.project.Inzynierka.servicelayer.errors.IllegalPasswordException;
import local.project.Inzynierka.servicelayer.errors.PasswordsNotMatchingException;
import local.project.Inzynierka.servicelayer.validation.PasswordCreatorService;
import local.project.Inzynierka.shared.AuthenticationFacade;
import local.project.Inzynierka.web.security.AccessPermissionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final VoivodeshipRepository voivodeshipRepository;

    private final NaturalPersonRepository naturalPersonRepository;

    private final AuthenticationFacade authenticationFacade;

    private final AddressRepository addressRepository;

    private final CompanyRepository companyRepository;

    private final PasswordCreatorService passwordCreatorService;

    private final AccessPermissionService accessPermissionService;

    public UserService(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, VoivodeshipRepository voivodeshipRepository, NaturalPersonRepository naturalPersonRepository, AuthenticationFacade authenticationFacade, AddressRepository addressRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, PasswordCreatorService passwordCreatorService, AccessPermissionService accessPermissionService) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.voivodeshipRepository = voivodeshipRepository;
        this.naturalPersonRepository = naturalPersonRepository;
        this.authenticationFacade = authenticationFacade;
        this.addressRepository = addressRepository;
        this.companyRepository = companyRepository;
        this.passwordCreatorService = passwordCreatorService;
        this.accessPermissionService = accessPermissionService;
    }

    public User findByName(String name) {

        return userRepository.findByName(name);
    }

    public User findByEmailAddress(EmailAddress emailAddress) {

        return userRepository.getByAddressEmail(emailAddress.getEmail());
    }

    public User createNewUser(User user) {

        user.setAccountType((short) 0);

        return userRepository.save(user);
    }

    @Transactional
    public void createVerificationTokenForUser(User user, final String token) {
        VerificationToken myToken = new VerificationToken(token);
        myToken.setId(0L);

        VerificationToken createdToken = verificationTokenRepository.save(myToken);
        user.setVerificationToken(createdToken);
        userRepository.save(user);
    }

    @Transactional
    public boolean verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return false;
        }

        User user = userRepository.findByVerificationToken(verificationToken);
        user.setEnabled(true);

        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean becomeNaturalPerson(NaturalPerson naturalPerson) {

        Voivoideship voivoideship = voivodeshipRepository.findByName(naturalPerson.getAddress().getVoivodeship_id().getName());
        if (voivoideship == null) {
            return false;
        }

        Address address = this.buildAddress(naturalPerson, voivoideship);

        address = addressRepository.save(address);
        naturalPerson.setAddress(address);
        naturalPerson.setId(0L);

        naturalPerson = naturalPersonRepository.save(naturalPerson);
        User user = userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());

        user.setNaturalPerson(naturalPerson);
        userRepository.save(user);

        return true;
    }

    private Address buildAddress(NaturalPerson naturalPerson, Voivoideship voivoideship) {

        return Address.builder()
                .apartmentNo(naturalPerson.getAddress().getApartmentNo())
                .buildingNo(naturalPerson.getAddress().getBuildingNo())
                .id(0L)
                .city(naturalPerson.getAddress().getCity())
                .voivodeship_id(voivoideship)
                .street(naturalPerson.getAddress().getStreet())
                .build();
    }

    public Optional<NaturalPerson> getUsersPersonalData(Long id, Long personID) {
        User authenticatedUser = this.authenticationFacade.getAuthenticatedUser();

        if (this.accessPermissionService.hasPrincipalHavePermissionToUserResource(id)) {
            if (authenticatedUser.hasRegisteredNaturalPerson() &&
                    authenticatedUser.getNaturalPerson().getId().equals(personID)) {
                return Optional.of(authenticatedUser.getNaturalPerson());
            }
        }

        return Optional.empty();
    }

    public Optional<AuthenticatedUserInfoDto> getUser(Long id) {

        if (accessPermissionService.hasPrincipalHavePermissionToUserResource(id)) {
            User authenticatedUser = authenticationFacade.getAuthenticatedUser();
            AuthenticatedUserInfoDto authenticatedUserInfoDto = new AuthenticatedUserInfoDto();
            if (authenticatedUser.hasRegisteredNaturalPerson()) {
                authenticatedUserInfoDto.setNaturalPersonID(authenticatedUser.getNaturalPerson().getId());
                authenticatedUserInfoDto.setCompaniesIDs(this.companyRepository
                                                                 .findByRegisterer(authenticatedUser.getNaturalPerson())
                                                                 .stream().map(Company::getId)
                                                                 .collect(Collectors.toList()));
            }
            authenticatedUserInfoDto.setEmailAddress(authenticatedUser.getEmailAddressEntity().getEmail());
            authenticatedUserInfoDto.setLoginName(authenticatedUser.getName());

            return Optional.of(authenticatedUserInfoDto);
        }

        return Optional.empty();
    }

    public boolean changePassword(UpdateUserDto updateUserDto, Long userId) {

        if (accessPermissionService.hasPrincipalHavePermissionToUserResource(userId)) {
            User user = this.authenticationFacade.getAuthenticatedUser();

            this.validatePasswordChange(updateUserDto, user);

            user.setPasswordHash(this.passwordCreatorService.encodePassword(updateUserDto.getNewPassword()));
            this.userRepository.save(user);

            return true;
        }

        return false;
    }

    private void validatePasswordChange(UpdateUserDto updateUserDto, User user) {
        if (!this.passwordCreatorService.comparePasswordHashes(
                updateUserDto.getCurrentPassword(), user.getPasswordHash())) {
            throw new PasswordsNotMatchingException("Provided password does not match your old password");
        }

        if (!this.passwordCreatorService.isPasswordValid(updateUserDto.getNewPassword())) {
            throw new IllegalPasswordException("Provided password has not passed the validation");
        }
    }

}
