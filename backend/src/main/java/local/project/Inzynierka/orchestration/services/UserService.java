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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private VoivodeshipRepository voivodeshipRepository;

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private AddressRepository addressRepository;

    public User findByName(String name) {

        User user = userRepository.findByName(name);
        return user;
    }

    public User findByEmailAddress(EmailAddress emailAddress) {
        User user = userRepository.getByAddressEmail(emailAddress.getEmail());

        return user;
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

        naturalPerson.setId(0L);

        Voivoideship voivoideship = voivodeshipRepository.findByName(naturalPerson.getAddress().getVoivodeship_id().getName());
        if (voivoideship == null) {
            return false;
        }

        Address address = Address.builder()
                .apartmentNo(naturalPerson.getAddress().getApartmentNo())
                .buildingNo(naturalPerson.getAddress().getApartmentNo())
                .id(0L)
                .city(naturalPerson.getAddress().getCity())
                .voivodeship_id(voivoideship)
                .street(naturalPerson.getAddress().getStreet())
                .build();

        address = addressRepository.save(address);
        naturalPerson.setAddress(address);

        naturalPerson = naturalPersonRepository.save(naturalPerson);
        User user = userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());

        user.setNaturalPerson(naturalPerson);
        userRepository.save(user);

        return true;
    }
}
