package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "tokens")
public class VerificationTokenEntity implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(name = "created_at")
   private Timestamp createdAt;

    public VerificationTokenEntity(String token) {
        this.token = token;
    }
}
