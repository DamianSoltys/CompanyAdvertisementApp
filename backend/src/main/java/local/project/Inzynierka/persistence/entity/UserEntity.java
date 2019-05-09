package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users")
public class UserEntity implements IEntity {


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
    private NaturalPersonEntity naturalPersonEntity;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_email_address",referencedColumnName = "email_id", unique = true, nullable = false, foreignKey = @ForeignKey(name = "email_FK"))
    private EmailAddressEntity emailAddressEntity;

    @Column(nullable = false, name = "account_type")
    private int accountType;

    public UserEntity() {
    }

    public UserEntity(long id, String name, String passwordHash, NaturalPersonEntity naturalPersonEntity, EmailAddressEntity emailAddressEntity, int accountType) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.naturalPersonEntity = naturalPersonEntity;
        this.emailAddressEntity = emailAddressEntity;
        this.accountType = accountType;
    }

    public static class UserBuilder {
        private long id;
        private String name;
        private String passwordHash;
        private NaturalPersonEntity naturalPersonEntity;
        private EmailAddressEntity emailAddressEntity;
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

        public UserBuilder setNaturalPersonEntity(NaturalPersonEntity naturalPersonEntity) {
            this.naturalPersonEntity = naturalPersonEntity;
            return this;
        }

        public UserBuilder setEmailAddressEntity(EmailAddressEntity emailAddressEntity) {
            this.emailAddressEntity = emailAddressEntity;
            return this;
        }

        public UserBuilder setAccountType(int accountType) {
            this.accountType = accountType;
            return this;
        }

        public UserEntity createUser() {
            return new UserEntity(id, name, passwordHash, naturalPersonEntity, emailAddressEntity, accountType);
        }
    }
}
