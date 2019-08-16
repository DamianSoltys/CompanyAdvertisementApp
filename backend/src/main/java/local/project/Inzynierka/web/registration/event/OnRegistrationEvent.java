package local.project.Inzynierka.web.registration.event;

public class OnRegistrationEvent {

    private final String userEmail;
    private final String appUrl;

    public OnRegistrationEvent(String userEmail, String appUrl) {
        this.userEmail = userEmail;
        this.appUrl = appUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getAppUrl() {
        return appUrl;
    }
}
