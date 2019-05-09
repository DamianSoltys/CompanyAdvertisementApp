package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "favourite_branches")
public class FavouriteBranchEntity implements Serializable {


   @EmbeddedId
   private PK pk;

   @OneToOne
   @JoinColumns({@JoinColumn(name = "user_id", referencedColumnName = "user_id", updatable = false, insertable = false)})
   private UserEntity userEntity;

   @OneToOne
   @JoinColumns({@JoinColumn(name = "branch_id", referencedColumnName = "branch_id", insertable = false, updatable = false)})
   private BranchEntity branchEntity;

    @Column(name = "created_at",  columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "modified_at",  columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;

}

@Embeddable
@Data
class PK implements Serializable{

    @Column(name = "user_id")
    private long userId;

    @Column(name = "branch_id")
    private long branchId;
}