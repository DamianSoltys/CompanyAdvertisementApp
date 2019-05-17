package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.domain.model.EmailAddress;
import local.project.Inzynierka.domain.model.User;

public interface UserService {

    User findByName(String name);

    User findByEmailAddress(EmailAddress emailAddress);

    User createNewUser(User user);
}
