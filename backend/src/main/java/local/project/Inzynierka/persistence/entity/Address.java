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
import javax.persistence.Table;

@Data
@Entity
@Table(name = "addresses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address extends FullTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voivodeship_id", nullable = false, foreignKey = @ForeignKey(name = "branch_voivodeship_FK"))
    private Voivoideship voivodeship_id;

    @Column(nullable = false, length = 30)
    private String city;

    @Column(length = 30)
    private String street;

    @Column(name = "apartment_no", length = 5)
    private String apartmentNo;

    @Column(nullable = false, length = 5, name = "building_no")
    private String buildingNo;

}
