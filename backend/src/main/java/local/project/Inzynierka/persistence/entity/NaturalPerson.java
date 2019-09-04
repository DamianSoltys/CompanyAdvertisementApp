package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "natural_persons")
public class NaturalPerson extends FullTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_natural_person")
    private Long id = 0L;

    @Column(nullable = false, name = "first_name", length = 30)
    private String firstName;

    @Column(name = "second_first_name", length = 30)
    private String secondFirstName;

    @Column(nullable = false, name = "last_name", length = 30)
    private String lastName;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(name = "address_natural_person_FK"))
    private Address address;

    @Column(nullable = false, name = "phone_no", unique = true, length = 13)
    private String phoneNo;

}
