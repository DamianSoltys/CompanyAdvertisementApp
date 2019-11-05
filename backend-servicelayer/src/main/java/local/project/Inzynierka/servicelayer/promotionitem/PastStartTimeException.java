package local.project.Inzynierka.servicelayer.promotionitem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PastStartTimeException extends RuntimeException {
    public PastStartTimeException(String message) {
        super(message);
    }
}
