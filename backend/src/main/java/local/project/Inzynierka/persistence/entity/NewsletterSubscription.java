package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "newsletter_subscriptions")
public class NewsletterSubscription implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "newsletter_company_FK"), nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_email", foreignKey = @ForeignKey(name = "newsletter_email_FK"), nullable = false)
    private EmailAddress emailAddressEntity;

    @Column(nullable = false)
    private boolean verified;

    @Column(nullable = false, name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(nullable = false, name = "modified_at", columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;

    @OneToOne
    @JoinColumn(name = "id_token",referencedColumnName = "token_id", unique = true, foreignKey = @ForeignKey(name = "verify_newsletter_token_FK"))
    private VerificationToken verificationToken;


}
