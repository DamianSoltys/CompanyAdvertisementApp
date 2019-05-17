package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity implements IEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 5000)
    private String comment;

    @Column(nullable = false, columnDefinition = "TIMESTAMP", name = "created_at")
    private Timestamp createdAt;

    @Column(nullable = false,  columnDefinition = "TIMESTAMP", name = "modified_at")
    private Timestamp modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "commenting_user_FK"))
    private UserEntity userEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false, foreignKey = @ForeignKey(name = "commentend_branch_FK"))
    private BranchEntity branchEntity;


}
