package local.project.Inzynierka.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "branches")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registerer_id", foreignKey = @ForeignKey(name = "branch_registerer_FK"), nullable = false)
    private NaturalPerson registerer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "company_branch_FK"), nullable = false)
    private Company company;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voivodeship_id", nullable = false, foreignKey = @ForeignKey(name = "branch_voivodeship_FK"))
    private Voivoideship voivodeship_id;

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
