package local.project.Inzynierka.servicelayer.dto.mapper;


import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.servicelayer.dto.address.Voivodeship;
import local.project.Inzynierka.servicelayer.dto.user.AuthenticatedUserPersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.user.BecomeNaturalPersonDto;
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
                                         .apartmentNo(becomeNaturalPersonDto.getAddress().getApartmentNo())
                                         .buildingNo(becomeNaturalPersonDto.getAddress().getBuildingNo())
                                         .city(becomeNaturalPersonDto.getAddress().getCity())
                                         .street(becomeNaturalPersonDto.getAddress().getStreet())
                                         .voivodeship_id(new Voivoideship((becomeNaturalPersonDto.getAddress().getVoivodeship().toString())))
                    .build());
        naturalPerson.setFirstName(becomeNaturalPersonDto.getFirstName());
        naturalPerson.setLastName(becomeNaturalPersonDto.getLastName());

        return naturalPerson;
    }

    public AuthenticatedUserPersonalDataDto map(NaturalPerson person) {

        AuthenticatedUserPersonalDataDto personDto = new AuthenticatedUserPersonalDataDto();
        personDto.setAddress(new local.project.Inzynierka.servicelayer.dto.address.Address(
                Voivodeship.fromVoivodeship(person.getAddress().getVoivodeship_id().getName().toLowerCase()),
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
