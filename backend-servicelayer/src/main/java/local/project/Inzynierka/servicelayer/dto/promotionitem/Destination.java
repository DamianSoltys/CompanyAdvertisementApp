package local.project.Inzynierka.servicelayer.dto.promotionitem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import local.project.Inzynierka.servicelayer.dto.social.SocialPlatform;

import java.util.Arrays;

public enum Destination {
    FB("fb"),
    TWITTER("twitter"),
    NEWSLETTER("newsletter");

    private String destination;

    Destination(String destination) {
        this.destination = destination;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.destination;
    }

    @JsonCreator
    public static Destination fromDestination(String value) {
        if(value == null ) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(Destination.values())
                .filter(v ->  v.toString().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
