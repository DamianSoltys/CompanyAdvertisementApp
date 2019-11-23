package local.project.Inzynierka.persistence.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum SendingStrategy {
    AT_CREATION("at_creation"),
    DELAYED("delayed"),
    AT_WILL("at_will");

    private String strategy;

    SendingStrategy(String strategy) {
        this.strategy = strategy;
    }

    @JsonCreator
    public static SendingStrategy fromSendingStrategy(String value) {
        if(value == null ) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(SendingStrategy.values())
                .filter(v ->  v.toString().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.strategy;
    }
}
