package local.project.Inzynierka.web.security;

import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.web.errors.BadLoginDataException;

public interface UserAuthenticationService {
    void registerNewUser(User user);
    User findUserByEmail(String email);
    String login(User user) throws BadLoginDataException;
}
