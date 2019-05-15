package local.project.Inzynierka.domain.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Company {
    private long id;
    private String name;
    private String NIP;
    private String REGON;
    private String voivodeship;
    private String city;
    private String street;
    /*
    * TODO Check alternative options for long string(text)
    * */
    private String description;
    private String buildingNo;
    private NaturalPerson registerer;
    private Category category;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    /*
    * TODO Check alternative options for storing photo.
    * */
    private String photoPath;
    private boolean hasBranch;
}
