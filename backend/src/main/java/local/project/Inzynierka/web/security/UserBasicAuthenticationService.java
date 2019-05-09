package local.project.Inzynierka.web.security;

import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.orchestration.mapper.UserMapper;
import local.project.Inzynierka.persistence.entity.EmailAddressEntity;
import local.project.Inzynierka.persistence.entity.UserEntity;
import local.project.Inzynierka.persistence.repository.EmailRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;


@Service
public class UserBasicAuthenticationService implements UserAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override @Transactional
    public void registerNewUser(User user) {


        Logger logger = LoggerFactory.getLogger("registerNewUser");
        logger.info(String.valueOf(user));

        UserEntity userEntity = userRepository.findByName(user.getName());
        EmailAddressEntity emailAddressEntity = emailRepository.findByEmail(user.getEmailAddress().getEmail());

        if(userEntity != null || emailAddressEntity != null){
            throw new IllegalStateException();
            // TODO REPLACE WITH CUSTOM EXCEPTION
        }

        Timestamp now = new Timestamp(Calendar.getInstance().getTime().getTime());

        emailAddressEntity = new EmailAddressEntity();
        emailAddressEntity.setEmail(user.getEmailAddress().getEmail());
        emailAddressEntity.setCreatedAt(now);
        emailAddressEntity = emailRepository.save(emailAddressEntity);


        userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setEmailAddressEntity(emailAddressEntity);
        userEntity.setPasswordHash(passwordEncoder.encode(user.getPassword()));


        userEntity.setCreatedAt(now);
        userEntity.setModifiedAt(now);

        userRepository.save(userEntity);

    }

    @Override
    public User findUserByEmail(String email) {

        Logger logger = LoggerFactory.getLogger("findUserByEmail");

        UserEntity userEntity = userRepository.getByAddressEmail(email);

        logger.info(String.valueOf(userEntity));

        if( userEntity == null ) {
            throw new NullPointerException();
        }

        return mapper.map(userEntity);
    }
}
