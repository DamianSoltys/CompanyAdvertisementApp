package local.project.Inzynierka.web.registration.event;

import local.project.Inzynierka.persistence.entity.User;

public class OnRegistrationEvent {

    private final User user;
    private final String appUrl;

    public OnRegistrationEvent(User user, String appUrl) {
        this.user = user;
        this.appUrl = appUrl;
    }

    public User getUser() {
        return user;
    }

    public String getAppUrl() {
        return appUrl;
    }

}
