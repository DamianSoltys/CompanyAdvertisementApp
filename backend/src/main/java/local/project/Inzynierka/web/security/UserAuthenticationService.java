package local.project.Inzynierka.web.security;

import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.web.errors.BadLoginDataException;
import local.project.Inzynierka.web.errors.EmailAlreadyTakenException;
import local.project.Inzynierka.web.errors.UserAlreadyExistsException;

public interface UserAuthenticationService {
    void registerNewUser(User user) throws UserAlreadyExistsException, EmailAlreadyTakenException;
    User findUserByEmail(String email);
    String login(User user) throws BadLoginDataException;
}
