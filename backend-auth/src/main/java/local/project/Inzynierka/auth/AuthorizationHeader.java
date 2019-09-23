package local.project.Inzynierka.auth;

import java.util.Base64;

public class AuthorizationHeader {

    private final String authorizationHeaderValue;

    public AuthorizationHeader(String name, String password) {
        authorizationHeaderValue = Base64.getEncoder().encodeToString((String.format("%s:%s", name, password)).getBytes());
    }

    public String getAuthorizationHeaderValue() {
        return this.authorizationHeaderValue;
    }
}
