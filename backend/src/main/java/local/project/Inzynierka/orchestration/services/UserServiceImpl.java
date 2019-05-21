package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.entity.VerificationToken;
import local.project.Inzynierka.persistence.repository.EmailRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.persistence.repository.VerificationTokenRepository;
import local.project.Inzynierka.shared.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @Override
    public User findByName(String name) {

        User user = userRepository.findByName(name);
        if( user == null ) {
            return null;
        }
        return user;
    }

    @Override
    public User findByEmailAddress(EmailAddress emailAddress) {
        User user = userRepository.getByAddressEmail(emailAddress.getEmail());

        if( user == null ) {
            return null;
        }

        return user;
    }

    @Override
    public User createNewUser(User user) {

        Timestamp now = DateUtils.getNowTimestamp();
        user.setModifiedAt(now);
        user.setCreatedAt(now);

        user = userRepository.save(user);

        return user;

    }

    @Override @Transactional
    public void createVerificationTokenForUser(User user, final String token) {
        Timestamp now = DateUtils.getNowTimestamp();
        VerificationToken myToken = new VerificationToken(token);
        myToken.setCreatedAt(now);
        myToken.setId(0L);

        VerificationToken createdToken = verificationTokenRepository.save(myToken);





    }
}
