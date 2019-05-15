package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "branches")
public class BranchEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "branch_registerer_FK"), nullable = false)
    private NaturalPersonEntity registerer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "company_branch_FK"), nullable = false)
    private CompanyEntity companyEntity;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voivodeship_id", nullable = false, foreignKey = @ForeignKey(name = "branch_voivodeship_FK"))
    private VoivoideshipEntity voivodeship_id;

    @Column(nullable = false, length = 30)
    private String city;

    @Column(length = 30)
    private String street;

    @Column(nullable = false, length = 5, name = "building_no")
    private String buildingNo;


    @Column(nullable = false, columnDefinition = "TIMESTAMP", name = "created_at")
    private Timestamp createdAt;

    @Column(nullable = false, name = "modified_at", columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;

    @Column(name = "x_geo_coordinate")
    private float geoX;

    @Column(name = "y_geo_coordinate")
    private float geoY;

}
