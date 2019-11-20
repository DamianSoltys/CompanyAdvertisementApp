package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "social_profiles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialProfile extends FullTimestampingAudit implements IEntity<SocialProfile.PK> {

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

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PK implements Serializable {

        @Column(name = "company_id")
        private Long companyId;

        @Column(name = "platform_id")
        private Short platformId;
    }

}
