package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "social_profiles")
public class SocialProfileUrl implements IEntity<SocialProfileUrl.PK> {

    @EmbeddedId
    private PK id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, updatable = false, insertable = false, foreignKey = @ForeignKey(name = "company_soc_prof_FK"))
    private Company company;

    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = false, updatable = false, insertable = false, foreignKey = @ForeignKey(name = "platform_FK"))
    private SocialMediaPlatform socialMediaPlatform;

    @Column(name = "social_profile_url", nullable = false)
    private String URL;

    @Column(nullable = false, name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(nullable = false, name = "modified_at", columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;

    @Embeddable
    @Data
    public class PK implements Serializable {

        @Column(name = "company_id")
        private Long companyId;

        @Column(name = "platform_id")
        private Short platformId;
    }


}
