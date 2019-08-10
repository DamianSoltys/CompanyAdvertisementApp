package local.project.Inzynierka;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.NaturalPersonRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.shared.utils.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NaturalPersonRepositoryTest {

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Autowired
    private VoivodeshipRepository voivodeshipRepository;

    private NaturalPerson naturalPerson;

    private static String phoneNo = "111222333";

    @Before
    public void setUp() {

        String firstName = "firstName";
        String lastName = "lastName";
        String appNo = "5d";
        String city =  "Lublin";
        String street = "street";
        String buildNo = "5d";


        naturalPerson = new NaturalPerson();
        naturalPerson.setFirstName(firstName);
        naturalPerson.setLastName(lastName);
        naturalPerson.setAddress(Address.builder()
                .apartmentNo(appNo)
                .build());
        naturalPerson.setId(0L);

        /*
        * FROM THE CONTEXT
        * */
        Voivoideship voivodeshipEntity = voivodeshipRepository.findById((short)1).get(); // IT RETURNS OPTIONAL
        /*
         *
         * */

        Timestamp now = DateUtils.getNowTimestamp();
        naturalPerson.setCreatedAt(now);
        naturalPerson.setModifiedAt(now);

        naturalPerson.setPhoneNo(phoneNo);
        naturalPerson.setAddress(Address.builder()
                .voivodeship_id(voivodeshipEntity)
                .buildingNo(buildNo)
                .city(city)
                .street(street)
                .build());
    }

    @Test
    public void testSavingTwicePersistsNaturalPersonTwice(){

        long beforeTestCount = naturalPersonRepository.count();
        long entititesToSave = 2;
        long expected = entititesToSave+beforeTestCount;

        naturalPersonRepository.save(naturalPerson);
        naturalPerson.setId(0L);  // New personal Entity - ID = 0
        naturalPerson.setPhoneNo("555"); // UNIQUE PHONE_NO
        naturalPersonRepository.save(naturalPerson);

        Assert.assertEquals(expected, naturalPersonRepository.count());
    }

    @Test
    public void testFindingByPhoneNo(){

       naturalPerson.setId(1L);
       naturalPersonRepository.save(naturalPerson);

        Assert.assertEquals(naturalPerson, naturalPersonRepository.findByPhoneNo(phoneNo));
    }




}
