package local.project.Inzynierka.persistence.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users")
@Builder(toBuilder = true)
public class User implements IEntity<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, name = "user_name", unique = true, length = 30)
    private String name;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @Column(nullable = false, name = "modified_at")
    private Timestamp modifiedAt;

    @Column(nullable = false, name = "created_at")
    private Timestamp createdAt;

    @OneToOne
    @JoinColumn(name = "id_natural_person", referencedColumnName = "id_natural_person",foreignKey = @ForeignKey(name = "natural_person_FK"))
    private NaturalPerson naturalPerson;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_email_address",referencedColumnName = "email_id", unique = true, nullable = false, foreignKey = @ForeignKey(name = "email_FK"))
    private EmailAddress emailAddressEntity;

    @Column(nullable = false, name = "account_type")
    private int accountType;

    @OneToOne
    @JoinColumn(name = "id_token",referencedColumnName = "token_id", unique = true, foreignKey = @ForeignKey(name = "verify_user_token_FK"))
    private VerificationToken verificationToken;


    @Column(name = "is_enabled")
    private boolean isEnabled;

    public User() {
    }

    /*
    *  The parameters order has to match the order of field's declarations in the class for lombok builder to work.
    * */
    public User(long id, String name, String passwordHash,Timestamp modifiedAt,
                Timestamp createdAt, NaturalPerson naturalPerson,
                EmailAddress emailAddressEntity, int accountType,
                VerificationToken verificationToken,boolean isEnabled ) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.naturalPerson = naturalPerson;
        this.emailAddressEntity = emailAddressEntity;
        this.accountType = accountType;
        this.isEnabled = isEnabled;
        this.verificationToken = verificationToken;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public User(String name, String passwordHash, EmailAddress emailAddressEntity) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.emailAddressEntity = emailAddressEntity;
    }


}
