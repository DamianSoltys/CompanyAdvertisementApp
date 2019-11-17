package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "favourite_branches")
public class FavouriteBranch extends FullTimestampingAudit implements IEntity<FavouriteBranch.PK> {


    @EmbeddedId
    private PK id;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", updatable = false, insertable = false)})
    private User user;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "branch_id", referencedColumnName = "branch_id", insertable = false, updatable = false)})
    private Branch branch;

    public FavouriteBranch() {
    }

    @Embeddable
    @Data
    public static class PK implements Serializable {

        @Column(name = "user_id")
        private long userId;

        @Column(name = "branch_id")
        private long branchId;
    }
}

