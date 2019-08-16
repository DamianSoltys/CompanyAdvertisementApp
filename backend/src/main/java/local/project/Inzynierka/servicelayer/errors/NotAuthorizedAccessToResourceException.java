package local.project.Inzynierka.servicelayer.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedAccessToResourceException extends RuntimeException {
    public NotAuthorizedAccessToResourceException(String s) {
        super(s);
    }
}
