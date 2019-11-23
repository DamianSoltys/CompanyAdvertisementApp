package local.project.Inzynierka.servicelayer.promotionitem.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidSendingStrategyException extends RuntimeException {
    private static final String ERROR_MESSAGE = "It is possible only to confirm sending of promotion item of at_will sending strategy.";

    public InvalidSendingStrategyException() {
        super(ERROR_MESSAGE);
    }
}
