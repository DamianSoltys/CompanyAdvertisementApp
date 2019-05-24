package local.project.Inzynierka.web.mapper;


import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.web.dto.BecomeNaturalPersonDto;
import org.springframework.stereotype.Component;

@Component
public class NaturalPersonDtoMapper {

    public NaturalPerson map(BecomeNaturalPersonDto becomeNaturalPersonDto) {
        if(becomeNaturalPersonDto == null ) {
            return null;
        }
        NaturalPerson naturalPerson = new NaturalPerson();

        naturalPerson.setPhoneNo(becomeNaturalPersonDto.getPhoneNo());
        naturalPerson.setApartmentNo(becomeNaturalPersonDto.getApartmentNo());
        naturalPerson.setBuildingNo(becomeNaturalPersonDto.getBuildingNo());
        naturalPerson.setCity(becomeNaturalPersonDto.getCity());
        naturalPerson.setFirstName(becomeNaturalPersonDto.getFirstName());
        naturalPerson.setLastName(becomeNaturalPersonDto.getLastName());
        naturalPerson.setVoivodeship(new Voivoideship(becomeNaturalPersonDto.getVoivodeship()));

        return naturalPerson;
    }
}
