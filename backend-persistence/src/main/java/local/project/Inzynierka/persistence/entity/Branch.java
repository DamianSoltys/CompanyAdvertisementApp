package local.project.Inzynierka.persistence.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

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
@Indexed
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Branch extends FullTimestampingAudit implements IEntity<Long>, SearchableEntity {

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

    @Field
    @Column(nullable = false, length = 50)
    private String name;

    @IndexedEmbedded
    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(name = "address_branch_FK"))
    private Address address;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "x_geo_coordinate")
    private Float geoX;

    @Column(name = "y_geo_coordinate")
    private Float geoY;
}
