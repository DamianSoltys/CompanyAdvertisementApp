package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "companies")
public class Company implements IEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = false, length = 10)
    private String NIP;

    @Column(unique = true, nullable = false, length = 14)
    private String REGON;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voivodeship_id", nullable = false, foreignKey = @ForeignKey(name = "company_voivodeship_FK"))
    private Voivoideship voivodeship_id;

    @Column(nullable = false, length = 40)
    private String city;

    @Column(length = 30)
    private String street;

    @Column(name = "building_no", nullable = false, length = 5)
    private String buildingNo;

    /*
    *  columnDefinition = "Text" --- NOT PORTABLE
    * */
    @Column(nullable = false, length = 10000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registerer_id", nullable = false, foreignKey = @ForeignKey(name = "company_registerer_FK"))
    private NaturalPerson registerer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "company_category_FK"))
    private Category category;

    @Column(name = "created_at", nullable = false,  columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;

    @Column(name = "photo_path", length = 50)
    private String photoPath;

    @Column(name = "has_branch", nullable = false)
    private boolean hasBranch;

}
