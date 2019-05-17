package local.project.Inzynierka.web.security;

import local.project.Inzynierka.domain.model.User;

public interface UserAuthenticationService {
    void registerNewUser(User user) ;
    String login(User user) ;
}
