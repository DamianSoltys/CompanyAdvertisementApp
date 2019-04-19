package local.project.Inzynierka.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "branches")
public class Branch implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "branch_registerer_FK"))
    private NaturalPerson registerer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "company_branch_FK"))
    private Company company;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 30)
    private String voivodeship;

    @Column(nullable = false, length = 30)
    private String city;

    @Column(length = 30)
    private String street;

    @Column(nullable = false, length = 5)
    private String buildingNo;


    @Column(nullable = false, columnDefinition = "TIMESTAMP", name = "created_at")
    private Timestamp createdAt;

    @Column(nullable = false, name = "modified_at", columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;

    @Column(name = "x_geo_cordinate")
    private float geoX;

    @Column(name = "y_geo_cordinate")
    private float geoY;

}
