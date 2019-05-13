package local.project.Inzynierka.web.security;

import local.project.Inzynierka.domain.model.User;

public interface UserAuthenticationService {
    void registerNewUser(User user);
    User findUserByEmail(String email);
    boolean login(User user);
}
