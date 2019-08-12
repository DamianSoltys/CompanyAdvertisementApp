package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.entity.VerificationToken;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.AddressRepository;
import local.project.Inzynierka.persistence.repository.NaturalPersonRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.persistence.repository.VerificationTokenRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.shared.AuthenticationFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final VoivodeshipRepository voivodeshipRepository;

    private final NaturalPersonRepository naturalPersonRepository;

    private final AuthenticationFacade authenticationFacade;

    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, VoivodeshipRepository voivodeshipRepository, NaturalPersonRepository naturalPersonRepository, AuthenticationFacade authenticationFacade, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.voivodeshipRepository = voivodeshipRepository;
        this.naturalPersonRepository = naturalPersonRepository;
        this.authenticationFacade = authenticationFacade;
        this.addressRepository = addressRepository;
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
        User authenticatedUser = this.userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());
        User requestedUser = this.userRepository.findById(id).orElse(new User());

        if (authenticatedUser.equals(requestedUser) &&
                authenticatedUser.hasRegisteredNaturalPerson() &&
                authenticatedUser.getNaturalPerson().getId().equals(personID)) {
            return Optional.of(authenticatedUser.getNaturalPerson());
        } else {
            return Optional.empty();
        }
    }
}
