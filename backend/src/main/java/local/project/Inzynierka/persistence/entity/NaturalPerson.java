package local.project.Inzynierka.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "natural_persons")
public class NaturalPerson implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_natural_person")
    private Long id;

    @Column(nullable = false, name = "first_name", length = 30)
    private String firstName;

    @Column(name = "second_first_name", length = 30)
    private String secondFirstName;

    @Column(nullable = false, name = "last_name", length = 30)
    private String lastName;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voivodeship_id", nullable = false, foreignKey = @ForeignKey(name = "natural_person_voivodeship_FK"))
    private Voivoideship voivodeship;

    @Column(nullable = false, length = 30)
    private String city;

    @Column(length = 30)
    private String street;

    @Column(name = "apartment_no", length = 5)
    private String apartmentNo;

    @Column(nullable = false, name = "building_no", length = 5)
    private String buildingNo;
    */

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(name = "address_natural_person_FK"))
    private Address address;


    @Column(nullable = false, name = "phone_no", unique = true, length = 13)
    private String phoneNo;

    @Column(nullable = false, name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(nullable = false, name = "modified_at", columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;



}
