package local.project.Inzynierka.orchestration.mapper;

import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.orchestration.errors.MappingException;
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

        if( userEntity == null ) {
            throw new MappingException();
        }
        User user = new User();

        user.setPassword(userEntity.getPasswordHash());
        user.setName(userEntity.getName());
        user.setAccountType(userEntity.getAccountType());
        user.setCreatedAt(userEntity.getCreatedAt());
        user.setModifiedAt(userEntity.getModifiedAt());
        user.setId(userEntity.getId());
        user.setEmailAddress(emailMapper.map(userEntity.getEmailAddressEntity()));
        user.setNaturalPerson(naturalPersonMapper.map(userEntity.getNaturalPersonEntity()));

        return user;
    }

    public UserEntity map(User user){
        if(user == null) {
            throw new MappingException();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setPasswordHash(user.getPassword());
        userEntity.setNaturalPersonEntity(naturalPersonMapper.map(user.getNaturalPerson()));
        userEntity.setAccountType(user.getAccountType());
        userEntity.setName(user.getName());
        userEntity.setCreatedAt(user.getCreatedAt());
        userEntity.setModifiedAt(user.getModifiedAt());
        userEntity.setId(0L); ///  CREATING NEW UserEntity - ID equals to 0
        userEntity.setEmailAddressEntity(emailMapper.map(user.getEmailAddress()));

        return userEntity;
    }
}
