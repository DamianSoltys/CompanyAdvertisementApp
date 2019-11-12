package local.project.Inzynierka.servicelayer.filestorage.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NO_CONTENT)
public class LackOfLogoException extends RuntimeException {

    private static final String MESSAGE = "Queried entity has not got any logo added.";

    public LackOfLogoException() {
        super(MESSAGE);
    }
}
