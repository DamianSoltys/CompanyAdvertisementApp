package local.project.Inzynierka.domain.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NewsletterSubscription {
    private long id;
    private Company company;
    private EmailAddress emailAddress;
    private boolean verified;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
}
