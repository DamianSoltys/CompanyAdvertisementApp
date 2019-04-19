package local.project.Inzynierka.persistence.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "companies")
public class Company implements IEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = false, length = 10)
    private String NIP;

    @Column(unique = true, nullable = false, length = 14)
    private String REGON;

    @Column(nullable = false, length = 30)
    private String voivodeship;

    @Column(nullable = false)
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
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "company_registerer_FK"))
    private NaturalPerson registerer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "company_cateoorie_FK"))
    private Category category;

    @Column(name = "created_at", nullable = false,  columnDefinition = "TIMESTAMP")
    private Date createdAt;

    @Column(name = "modified_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;

    @Column(name = "photo_path", nullable = false, length = 50)
    private String photoPath;

    @Column(name = "has_branch", nullable = false)
    private Boolean hasBranch;

}
