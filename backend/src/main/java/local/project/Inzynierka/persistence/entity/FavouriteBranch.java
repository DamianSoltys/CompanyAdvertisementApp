package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "favourite_branches")
public class FavouriteBranch implements IEntity<FavouriteBranch.PK> {


   @EmbeddedId
   private PK id;

   @OneToOne
   @JoinColumns({@JoinColumn(name = "user_id", referencedColumnName = "user_id", updatable = false, insertable = false)})
   private User user;

   @OneToOne
   @JoinColumns({@JoinColumn(name = "branch_id", referencedColumnName = "branch_id", insertable = false, updatable = false)})
   private Branch branch;

    @Column(name = "created_at",  columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "modified_at",  columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;

    @Embeddable
    @Data
    public class PK implements Serializable{

        @Column(name = "user_id")
        private long userId;

        @Column(name = "branch_id")
        private long branchId;
    }
}

