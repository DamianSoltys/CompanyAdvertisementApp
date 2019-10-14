package local.project.Inzynierka.servicelayer.filestorage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
class UnsupportedLogoFileFormat extends RuntimeException {

    private static final String SUPPORTED_MEDIA_TYPES = "image/jpeg and image/png are only supported.";

    UnsupportedLogoFileFormat() {
        super(SUPPORTED_MEDIA_TYPES);
    }
}
