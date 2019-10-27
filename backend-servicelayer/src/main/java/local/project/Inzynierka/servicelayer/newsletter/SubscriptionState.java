package local.project.Inzynierka.servicelayer.newsletter;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SubscriptionState {
    SUBSCRIBED("subscribed"),
    PENDING("pending"),
    SAVED("saved");

    private String state;

    @JsonValue
    @Override
    public String toString() {
        return super.toString();
    }
}
