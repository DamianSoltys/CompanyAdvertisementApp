package local.project.Inzynierka.web.mapper;


import local.project.Inzynierka.persistence.entity.Address;
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
        naturalPerson.setAddress(Address.builder()
                    .apartmentNo(becomeNaturalPersonDto.getApartmentNo())
                .buildingNo(becomeNaturalPersonDto.getBuildingNo())
                    .city(becomeNaturalPersonDto.getCity())
                    .voivodeship_id(new Voivoideship((becomeNaturalPersonDto.getVoivodeship())))
                    .build());
        naturalPerson.setFirstName(becomeNaturalPersonDto.getFirstName());
        naturalPerson.setLastName(becomeNaturalPersonDto.getLastName());

        return naturalPerson;
    }
}
