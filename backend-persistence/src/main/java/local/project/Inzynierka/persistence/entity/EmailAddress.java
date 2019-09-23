package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.CreationTimestampingAudit;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "email_addresses")
public class EmailAddress extends CreationTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long id = 0L;

    @Column(unique = true, nullable = false, length = 254, name = "email")
    private String email;

    public EmailAddress(String email) {
        this.email = email;
    }

    public EmailAddress() {
    }
}
