package local.project.Inzynierka.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users")
public class User implements IEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(nullable = false, name = "user_name", unique = true, length = 30)
    private String name;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @Column(nullable = false, name = "modified_at", columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;

    @Column(nullable = false, name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @OneToOne
    @JoinColumn(name = "id_natural_person", referencedColumnName = "id_natural_person",foreignKey = @ForeignKey(name = "natural_person_FK"))
    private NaturalPerson naturalPerson;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_email_address",referencedColumnName = "email_id", unique = true, nullable = false, foreignKey = @ForeignKey(name = "email_FK"))
    private EmailAddress emailAddress;

    @Column(nullable = false, name = "account_type")
    private int accountType;

    public User() {
    }

    public User(long id, String name, String passwordHash, NaturalPerson naturalPerson, EmailAddress emailAddress, int accountType) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.naturalPerson = naturalPerson;
        this.emailAddress = emailAddress;
        this.accountType = accountType;
    }

    public static class UserBuilder {
        private long id;
        private String name;
        private String passwordHash;
        private NaturalPerson naturalPerson;
        private EmailAddress emailAddress;
        private int accountType;

        public UserBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public UserBuilder setNaturalPerson(NaturalPerson naturalPerson) {
            this.naturalPerson = naturalPerson;
            return this;
        }

        public UserBuilder setEmailAddress(EmailAddress emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public UserBuilder setAccountType(int accountType) {
            this.accountType = accountType;
            return this;
        }

        public User createUser() {
            return new User(id, name, passwordHash, naturalPerson, emailAddress, accountType);
        }
    }
}
