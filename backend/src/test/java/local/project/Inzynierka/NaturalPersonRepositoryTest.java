package local.project.Inzynierka;

import local.project.Inzynierka.persistence.entity.NaturalPersonEntity;
import local.project.Inzynierka.persistence.entity.VoivoideshipEntity;
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

    private NaturalPersonEntity naturalPersonEntity;

    private static String phoneNo = "111222333";

    @Before
    public void setUp() {

        String firstName = "firstName";
        String lastName = "lastName";
        String appNo = "5d";
        String city =  "Lublin";
        String street = "street";
        String buildNo = "5d";


        naturalPersonEntity = new NaturalPersonEntity();
        naturalPersonEntity.setFirstName(firstName);
        naturalPersonEntity.setLastName(lastName);
        naturalPersonEntity.setApartmentNo(appNo);
        naturalPersonEntity.setId(0L);

        /*
        * FROM THE CONTEXT
        * */
        VoivoideshipEntity voivodeshipEntity = voivodeshipRepository.findById((short)1).get(); // IT RETURNS OPTIONAL
        /*
         *
         * */
        naturalPersonEntity.setVoivodeship_id(voivodeshipEntity);

        naturalPersonEntity.setBuildingNo(buildNo);
        naturalPersonEntity.setCity(city);

        Timestamp now = DateUtils.getNowTimestamp();
        naturalPersonEntity.setCreatedAt(now);
        naturalPersonEntity.setModifiedAt(now);

        naturalPersonEntity.setPhoneNo(phoneNo);
        naturalPersonEntity.setStreet(street);
    }

    @Test
    public void testSavingTwicePersistsNaturalPersonTwice(){

        long beforeTestCount = naturalPersonRepository.count();
        long entititesToSave = 2;
        long expected = entititesToSave+beforeTestCount;

        naturalPersonRepository.save(naturalPersonEntity);
        naturalPersonEntity.setId(0L);  // New personal Entity - ID = 0
        naturalPersonEntity.setPhoneNo("555"); // UNIQUE PHONE_NO
        naturalPersonRepository.save(naturalPersonEntity);

        Assert.assertEquals(expected, naturalPersonRepository.count());
    }

    @Test
    public void testFindingByPhoneNo(){

       naturalPersonEntity.setId(1L);
       naturalPersonRepository.save(naturalPersonEntity);

        Assert.assertEquals(naturalPersonEntity, naturalPersonRepository.findByPhoneNo(phoneNo));
    }




}
