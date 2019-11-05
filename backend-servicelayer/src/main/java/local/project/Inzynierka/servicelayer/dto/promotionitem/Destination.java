package local.project.Inzynierka.servicelayer.dto.promotionitem;

public enum Destination {
    FB("fb"),
    TWITTER("twitter"),
    NEWSLETTER("newsletter");

    private String destination;

    Destination(String destination) {
        this.destination = destination;
    }
}
