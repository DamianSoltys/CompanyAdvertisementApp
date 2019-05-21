package local.project.Inzynierka.web.security;

import local.project.Inzynierka.persistence.entity.User;

public interface UserAuthenticationService {
    void registerNewUser(User user) ;
    String login(User user) ;
}
