package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fb_tokens")
public class FacebookToken extends FullTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facebook_token_id")
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;

    @Column(name = "data_access_expires_at", nullable = false)
    private Long dataAccessExpiresAt;

    @Column(name = "issued_at")
    private Long issuedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facebook_profile_id",nullable = false, foreignKey = @ForeignKey(name = "profile_facebook_token_FK"))
    private FacebookSocialProfile facebookSocialProfile;
}
