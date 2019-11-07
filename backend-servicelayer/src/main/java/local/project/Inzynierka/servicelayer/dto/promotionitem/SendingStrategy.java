package local.project.Inzynierka.servicelayer.dto.promotionitem;

public enum SendingStrategy {
    AT_CREATION("at_creation"),
    DELAYED("delayed"),
    AT_WILL("at_will");

    private String strategy;

    SendingStrategy(String strategy) {
        this.strategy = strategy;
    }
}
