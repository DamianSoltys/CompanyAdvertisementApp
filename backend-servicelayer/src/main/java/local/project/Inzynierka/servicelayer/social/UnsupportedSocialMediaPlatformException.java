package local.project.Inzynierka.servicelayer.social;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UnsupportedSocialMediaPlatformException extends RuntimeException {

    private static final String NOT_SUPPORTED_SOCIAL_MEDIA_PLATFORM = "NOT SUPPORTED SOCIAL MEDIA PLATFORM";

    public UnsupportedSocialMediaPlatformException() {
        super(NOT_SUPPORTED_SOCIAL_MEDIA_PLATFORM);
    }
}
