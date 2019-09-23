package local.project.Inzynierka.shared;

public interface UsernamePasswordAuthentication extends UserAccount {

    @Override
    default Long getId() {
        return null;
    }

    @Override
    default String getLoginName() {
        return null;
    }

    @Override
    default Boolean isEnabled() {
        return null;
    }

    @Override
    default Boolean isNaturalPersonRegistered() {
        return null;
    }

    @Override
    default Long personId() {
        return null;
    }
}
