package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.CreationTimestampingAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fb_token_scopes")
@Builder
public class FacebookTokenScope extends CreationTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facebook_token_scope_id")
    private Long id;

    @Column(name = "token_scope_type")
    @Enumerated(value = EnumType.STRING)
    private TokenScopeType tokenScopeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facebook_token_id", nullable = false, foreignKey = @ForeignKey(name = "scope_facebook_token_FK"))
    private FacebookToken facebookToken;

    @Column(name = "scope", nullable = false)
    private String scope;
}
