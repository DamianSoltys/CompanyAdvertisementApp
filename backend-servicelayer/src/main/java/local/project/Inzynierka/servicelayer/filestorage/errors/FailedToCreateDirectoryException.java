package local.project.Inzynierka.servicelayer.filestorage.errors;


public class FailedToCreateDirectoryException extends RuntimeException {
    public FailedToCreateDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
