package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fb_social_profiles")
@Builder
public class FacebookSocialProfile extends FullTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facebook_social_profile_id")
    private Long id;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumns(value = {
            @JoinColumn(name = "company_id", referencedColumnName = "company_id", foreignKey = @ForeignKey(name = "company_fb_profile_FK")),
            @JoinColumn(name = "platform_id", referencedColumnName = "platform_id", foreignKey = @ForeignKey(name = "platform_social_platform_FK"))
    })
    private SocialProfile socialProfile;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "page_id", unique = true)
    private Long pageId;

}
