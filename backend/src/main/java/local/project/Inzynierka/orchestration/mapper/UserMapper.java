package local.project.Inzynierka.orchestration.mapper;

import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.persistence.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private NaturalPersonMapper naturalPersonMapper;

    public User map(UserEntity userEntity) {

        Logger logger = LoggerFactory.getLogger("UserMapper");
        logger.info(String.valueOf(userEntity));

        if( userEntity == null ) {
            throw new NullPointerException();
        }
        User user = new User();
        logger.info(String.valueOf(user));

        user.setPassword(userEntity.getPasswordHash());
        logger.info(String.valueOf(user));

        user.setName(userEntity.getName());
        logger.info(String.valueOf(user));

        user.setAccountType(userEntity.getAccountType());
        logger.info(String.valueOf(user));

        user.setCreatedAt(userEntity.getCreatedAt());
        logger.info(String.valueOf(user));

        user.setModifiedAt(userEntity.getModifiedAt());
        logger.info(String.valueOf(user));

        user.setId(userEntity.getId());
        logger.info(String.valueOf(user));


        user.setEmailAddress(emailMapper.map(userEntity.getEmailAddressEntity()));
        logger.info(String.valueOf(user));

        user.setNaturalPerson(naturalPersonMapper.map(userEntity.getNaturalPersonEntity()));
        logger.info(String.valueOf(user));



        logger.info("END");

        return user;
    }


    // TODO Implement the mapping
    public UserEntity map(User user){
        return null;
    }
}
