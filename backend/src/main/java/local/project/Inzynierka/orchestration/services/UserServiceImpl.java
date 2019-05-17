package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.domain.model.EmailAddress;
import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.orchestration.mapper.UserMapper;
import local.project.Inzynierka.persistence.entity.UserEntity;
import local.project.Inzynierka.persistence.repository.EmailRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.shared.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByName(String name) {

        UserEntity userEntity = userRepository.findByName(name);
        if( userEntity == null ) {
            return null;
        }
        return userMapper.map(userEntity);
    }

    @Override
    public User findByEmailAddress(EmailAddress emailAddress) {
        UserEntity userEntity = userRepository.getByAddressEmail(emailAddress.getEmail());

        if( userEntity == null ) {
            return null;
        }

        return userMapper.map(userEntity);
    }

    @Override
    public User createNewUser(User user) {

        Timestamp now = DateUtils.getNowTimestamp();
        user.setModifiedAt(now);
        user.setCreatedAt(now);

        var userEntity = userMapper.map(user);
        userEntity = userRepository.save(userEntity);

        return userMapper.map(userEntity);

    }
}
