package local.project.Inzynierka.servicelayer.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TooManySearchParameterValuesException extends RuntimeException {

    public TooManySearchParameterValuesException(int upperParameterNumberLimit) {
        super(String.format("Exceeded limit of %s of search parameter values.", upperParameterNumberLimit));
    }
}
