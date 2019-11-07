package local.project.Inzynierka.servicelayer.dto.promotionitem;

public enum SendingStatus {
    SENT("sent"), DELAYED("delayed"), WAITING_FOR_ACTION("waiting_for_action");

    private String status;

    SendingStatus(String sendingStatus) {
        this.status = sendingStatus;
    }
}
