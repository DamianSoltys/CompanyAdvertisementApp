package local.project.Inzynierka.servicelayer.errors;

public class PasswordsNotMatchingException extends RuntimeException {
    public PasswordsNotMatchingException(String message) {
        super(message);
    }
}
