package local.project.Inzynierka.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class LogoKeyDoesNotMatchRequestParameterException extends RuntimeException {

    private static final String MESSAGE = "Logo key has to match request's logokey parameter";

    public LogoKeyDoesNotMatchRequestParameterException() {
        super(MESSAGE);
    }
}
