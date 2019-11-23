package local.project.Inzynierka.servicelayer.dto.promotionitem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum SendingStatus {
    SENT("sent"), DELAYED("delayed"), WAITING_FOR_ACTION("waiting_for_action"), FAILED("failed");

    private String status;

    SendingStatus(String sendingStatus) {
        this.status = sendingStatus;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.status;
    }

    @JsonCreator
    public static SendingStatus fromSendingStatus(String value) {
        if(value == null ) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(SendingStatus.values())
                .filter(v ->  v.toString().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
