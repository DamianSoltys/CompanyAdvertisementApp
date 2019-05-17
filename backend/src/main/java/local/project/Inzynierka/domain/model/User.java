package local.project.Inzynierka.domain.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Data
public class User {

    private long id;
    private String name;
    private String password;
    private Timestamp modifiedAt;
    private Timestamp createdAt;
    private NaturalPerson naturalPerson;
    private EmailAddress emailAddress;
    private int accountType;

    public User(String name, String password, EmailAddress emailAddress) {
        this.name = name;
        this.password = password;
        this.emailAddress = emailAddress;
    }

    public User() {
    }


}
