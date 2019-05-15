package local.project.Inzynierka.domain.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Branch {

    private long id;
    private NaturalPerson registerer;
    private Company company;
    private String name;
    private String voivodeship;
    private String city;
    private String street;
    private String buildingNo;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    private float geoX;
    private float  geoY;

}
