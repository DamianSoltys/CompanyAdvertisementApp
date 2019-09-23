package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.CreationTimestampingAudit;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "tokens")
public class VerificationToken extends CreationTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id = 0L;

    @Column(unique = true, nullable = false)
    private String token;

    public VerificationToken(String token) {
        this.token = token;
    }

    public VerificationToken() {
    }
}
