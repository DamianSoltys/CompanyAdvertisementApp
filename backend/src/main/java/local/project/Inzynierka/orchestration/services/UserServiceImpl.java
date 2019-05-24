package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.*;
import local.project.Inzynierka.persistence.repository.*;
import local.project.Inzynierka.shared.utils.DateUtils;
import local.project.Inzynierka.shared.AuthenticationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private VoivodeshipRepository voivodeshipRepository;

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public User findByName(String name) {

        User user = userRepository.findByName(name);
        return user;
    }

    @Override
    public User findByEmailAddress(EmailAddress emailAddress) {
        User user = userRepository.getByAddressEmail(emailAddress.getEmail());

        return user;
    }

    @Override
    public User createNewUser(User user) {

        Timestamp now = DateUtils.getNowTimestamp();
        user.setModifiedAt(now);
        user.setCreatedAt(now);

        return userRepository.save(user);

    }

    @Override @Transactional
    public void createVerificationTokenForUser(User user, final String token) {
        Timestamp now = DateUtils.getNowTimestamp();
        VerificationToken myToken = new VerificationToken(token);
        myToken.setCreatedAt(now);
        myToken.setId(0L);

        VerificationToken createdToken = verificationTokenRepository.save(myToken);
        user.setVerificationToken(createdToken);
        userRepository.save(user);
    }

    @Override @Transactional
    public boolean verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if( verificationToken == null ) {
            return false;
        }

        User user = userRepository.findByVerificationToken(verificationToken);
        log.info(String.valueOf(user));

        user.setEnabled(true);
        user.setModifiedAt(DateUtils.getNowTimestamp());

        userRepository.save(user);
        return true;
    }

    @Override @Transactional
    public boolean becomeNaturalPerson(NaturalPerson naturalPerson) {

        Timestamp now = DateUtils.getNowTimestamp();
        naturalPerson.setCreatedAt(now);
        naturalPerson.setModifiedAt(now);
        naturalPerson.setId(0L);

        Voivoideship voivoideship = voivodeshipRepository.findByName(naturalPerson.getVoivodeship().getName());
        if(voivoideship == null ) {
            return false;
        }

        naturalPerson.setVoivodeship(voivoideship);
        naturalPerson = naturalPersonRepository.save(naturalPerson);

        User user = userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());

        user.setNaturalPerson(naturalPerson);
        user.setModifiedAt(now);
        userRepository.save(user);

        return true;
    }
}
