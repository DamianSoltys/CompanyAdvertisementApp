package local.project.Inzynierka.domain.model;

import java.sql.Timestamp;
import java.util.Objects;

public class User {

    private long id;

    private String name;

    private String password;

    private Timestamp modifiedAt;

    private Timestamp createdAt;

    private NaturalPerson naturalPerson;

    private EmailAddress emailAddress;

    private int accountType;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public NaturalPerson getNaturalPerson() {
        return naturalPerson;
    }

    public void setNaturalPerson(NaturalPerson naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id &&
                accountType == user.accountType &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                Objects.equals(modifiedAt, user.modifiedAt) &&
                Objects.equals(createdAt, user.createdAt) &&
                Objects.equals(naturalPerson, user.naturalPerson) &&
                Objects.equals(emailAddress, user.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, modifiedAt, createdAt, naturalPerson, emailAddress, accountType);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", modifiedAt=" + modifiedAt +
                ", createdAt=" + createdAt +
                ", naturalPerson=" + naturalPerson +
                ", emailAddress=" + emailAddress +
                ", accountType=" + accountType +
                '}';
    }
}
