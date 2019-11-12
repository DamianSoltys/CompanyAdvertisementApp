package local.project.Inzynierka.servicelayer.filestorage.errors;

import java.io.IOException;

public class FailedToSaveLogoException extends RuntimeException {
    public FailedToSaveLogoException(String message, IOException e) {
        super(message, e);
    }
}
