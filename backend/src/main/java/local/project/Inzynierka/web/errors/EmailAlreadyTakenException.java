package local.project.Inzynierka.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class EmailAlreadyTakenException extends WebException{

    private static final long serialVersionUID = 1L;
    private static final String message = "Adres email jest już zajęty.";

    public EmailAlreadyTakenException() {
        super(message);
    }
}
