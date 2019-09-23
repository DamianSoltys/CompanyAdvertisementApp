package local.project.Inzynierka.shared.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class UserAlreadyExistsException extends WebException{

    private static final long serialVersionUID = 1L;
    private static final String message = "Nazwa użytkownika jest już zajęta.";

    public UserAlreadyExistsException() {
        super(message);
    }
}
