package local.project.Inzynierka.shared;

public interface UserAccount {

    Long getId();

    String getEmail();

    String getLoginName();

    String getPassword();

    Boolean isEnabled();

    Boolean isNaturalPersonRegistered();

    Long personId();
}
