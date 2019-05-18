package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "ratings")
public class RatingEntity implements IEntity<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private int rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,foreignKey = @ForeignKey(name = "rating_user_FK"))
    private UserEntity userEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false, foreignKey = @ForeignKey(name = "rated_FK"))
    private BranchEntity branchEntity;

    @Column(nullable = false, name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(nullable = false, name = "modified_at", columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;


}
