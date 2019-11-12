package local.project.Inzynierka.servicelayer.filestorage.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class UnsupportedLogoFileFormatException extends RuntimeException {

    private static final String SUPPORTED_MEDIA_TYPES = "image/jpeg and image/png are only supported.";

    public UnsupportedLogoFileFormatException() {
        super(SUPPORTED_MEDIA_TYPES);
    }
}
