package local.project.Inzynierka;


import local.project.Inzynierka.persistence.common.VoivodeshipUpdateException;
import local.project.Inzynierka.persistence.entity.VoivoideshipEntity;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VoivodeshipRepositoryTest {

    @Autowired
    private VoivodeshipRepository voivodeshipRepository;


    @Test(expected = VoivodeshipUpdateException.class)
    public void testCannotUpdateVoivodeshipsTable(){
        VoivoideshipEntity voivoideshipEntity = new VoivoideshipEntity();

        voivodeshipRepository.save(voivoideshipEntity);
    }

    @Test(expected = VoivodeshipUpdateException.class)
    public void testCannotRemoveAnyVoivoideship() {
        VoivoideshipEntity voivoideshipEntity = new VoivoideshipEntity();

        voivodeshipRepository.delete(voivoideshipEntity);
    }

    @Test
    public void testReturnCorrectNumberOfVoivodeships(){

        int correctNumber = 16;

        assertEquals(correctNumber, voivodeshipRepository.count());
    }
}
