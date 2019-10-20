package local.project.Inzynierka.servicelayer.errors;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class FailedToPersistBranchException extends RuntimeException {
}
