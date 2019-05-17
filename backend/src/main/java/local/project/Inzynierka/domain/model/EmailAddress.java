package local.project.Inzynierka.domain.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class EmailAddress {

    private long id;
    private String email;
    private Timestamp createdAt;

    public EmailAddress(String email) {
        this.email = email;
    }

    public EmailAddress() {
    }
}
