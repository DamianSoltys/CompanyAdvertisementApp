package local.project.Inzynierka.web.security;

import java.util.Base64;

public class AuthorizationHeader {

    private final String authorizationHeaderValue;

    public AuthorizationHeader(String name, String password) {
        authorizationHeaderValue = Base64.getEncoder().encodeToString((name+":"+password).getBytes());
    }

    public String getAuthorizationHeaderValue() {
        return this.authorizationHeaderValue;
    }
}
