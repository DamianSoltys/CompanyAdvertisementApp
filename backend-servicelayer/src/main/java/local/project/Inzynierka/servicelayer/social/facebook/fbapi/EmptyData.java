package local.project.Inzynierka.servicelayer.social.facebook.fbapi;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EmptyData {
    EMPTY_DATA("{\"data\":[]}");

    String emptyData;

    EmptyData(String emptyData) {
        this.emptyData = emptyData;
    }

    @JsonValue
    @Override
    public String toString() {
        return emptyData;
    }
}
