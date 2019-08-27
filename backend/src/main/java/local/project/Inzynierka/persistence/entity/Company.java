package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
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
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "companies")
public class Company extends FullTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = false, length = 10)
    private String NIP;

    @Column(unique = true, nullable = false, length = 14)
    private String REGON;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(name = "address_company_FK"))
    private Address address;

    @Column(nullable = false, length = 10000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registerer_id", nullable = false, foreignKey = @ForeignKey(name = "company_registerer_FK"))
    private NaturalPerson registerer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "company_category_FK"))
    private Category category;

    @Column(name = "logo_path")
    private String logoPath;

    @Column(name = "company_website")
    private String companyWebsiteLink;

    @Getter(AccessLevel.NONE)
    @Column(name = "has_branch", nullable = false)
    private boolean hasBranch;

    public boolean hasBranch() {
        return this.hasBranch;
    }

}
