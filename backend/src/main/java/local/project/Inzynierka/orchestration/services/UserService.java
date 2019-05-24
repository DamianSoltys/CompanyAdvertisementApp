package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.User;

public interface UserService {

    User findByName(String name);

    User findByEmailAddress(EmailAddress emailAddress);

    User createNewUser(User user);

    void createVerificationTokenForUser(User user, final String token);

    boolean verifyToken(String token);

    boolean becomeNaturalPerson(NaturalPerson naturalPerson);
}
