package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "branches")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch extends FullTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long id = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registerer_id", foreignKey = @ForeignKey(name = "branch_registerer_FK"), nullable = false)
    private NaturalPerson registerer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "company_branch_FK"), nullable = false)
    private Company company;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(name = "address_branch_FK"))
    private Address address;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "x_geo_coordinate")
    private float geoX;

    @Column(name = "y_geo_coordinate")
    private float geoY;
}
