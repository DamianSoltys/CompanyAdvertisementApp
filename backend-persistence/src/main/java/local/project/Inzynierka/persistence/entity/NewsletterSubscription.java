package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "newsletter_subscriptions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterSubscription extends FullTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "newsletter_company_FK"), nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_email", foreignKey = @ForeignKey(name = "newsletter_email_FK"), nullable = false)
    private EmailAddress emailAddressEntity;

    @Column(nullable = false)
    private boolean verified;

    @OneToOne
    @JoinColumn(name = "id_verification_token", referencedColumnName = "token_id", unique = true, foreignKey = @ForeignKey(name = "verify_newsletter_token_FK"))
    private VerificationToken verificationToken;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_unsubscribe_token", referencedColumnName = "token_id", unique = true, foreignKey = @ForeignKey(name = "unsubscribe_newsletter_token_FK"))
    private VerificationToken unsubscribeToken;

}
