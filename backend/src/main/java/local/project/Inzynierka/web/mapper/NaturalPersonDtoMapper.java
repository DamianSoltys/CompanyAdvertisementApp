package local.project.Inzynierka.web.mapper;


import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.web.dto.AuthenticatedUserInfoDto;
import local.project.Inzynierka.web.dto.BecomeNaturalPersonDto;
import local.project.Inzynierka.web.dto.Voivodeship;
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

    public AuthenticatedUserInfoDto map(NaturalPerson person) {

        AuthenticatedUserInfoDto personDto = new AuthenticatedUserInfoDto();
        personDto.setAddress(new local.project.Inzynierka.web.dto.Address(
                Voivodeship.fromVoivodeship(person.getAddress().getVoivodeship_id().getName()),
                person.getAddress().getCity(),
                person.getAddress().getStreet(),
                person.getAddress().getBuildingNo(),
                person.getAddress().getApartmentNo()
        ));
        personDto.setFirstName(person.getFirstName());
        personDto.setLastName(person.getLastName());
        personDto.setPhoneNo(person.getPhoneNo());

        return personDto;
    }
}
