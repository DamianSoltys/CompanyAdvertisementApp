package local.project.Inzynierka.servicelayer.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidVoivodeshipException extends RuntimeException {
}
