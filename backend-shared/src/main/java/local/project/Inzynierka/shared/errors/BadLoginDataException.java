package local.project.Inzynierka.shared.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class BadLoginDataException extends WebException{
    private static final long serialVersionUID = 1L;

    private static final String message = "Podany email bądź hasło są nieprawidłowe.";
    public BadLoginDataException() {
        super(message);

    }

    public int getStatus(){
        return HttpStatus.UNAUTHORIZED.value();
    }

}
