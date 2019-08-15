package local.project.Inzynierka.web.security;

import local.project.Inzynierka.persistence.entity.User;

public interface UserAuthenticationService {
    void registerNewUser(User user) ;

    Long login(User user);
    boolean confirmUser(String token);
}
