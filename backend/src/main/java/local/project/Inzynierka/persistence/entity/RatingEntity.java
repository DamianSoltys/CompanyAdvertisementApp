package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "ratings")
public class RatingEntity implements IEntity<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,foreignKey = @ForeignKey(name = "rating_user_FK"))
    private UserEntity userEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "rated_FK"))
    private BranchEntity branchEntity;

    @Column(nullable = false, name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(nullable = false, name = "modified_at", columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;


}
