package local.project.Inzynierka.servicelayer.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UnexistingBranchException extends RuntimeException {
    public UnexistingBranchException(){
        super("Branch with that id doesn't exist");
    }
}
