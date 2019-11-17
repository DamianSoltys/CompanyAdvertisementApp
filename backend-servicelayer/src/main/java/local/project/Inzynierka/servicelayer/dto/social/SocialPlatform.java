package local.project.Inzynierka.servicelayer.dto.social;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum SocialPlatform {
    FACEBOOK("facebook"), TWITTER("twitter");

    private String platform;

    SocialPlatform(String platform) {
        this.platform = platform;
    }

    @JsonValue
    @Override
    public String toString() {
        return super.toString();
    }

    @JsonCreator
    public static SocialPlatform fromSocialPlatform(String value) {
        if(value == null ) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(SocialPlatform.values())
                .filter(v ->  v.toString().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }


}
