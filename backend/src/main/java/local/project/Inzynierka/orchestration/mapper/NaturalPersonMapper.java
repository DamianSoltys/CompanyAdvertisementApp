package local.project.Inzynierka.orchestration.mapper;

import local.project.Inzynierka.domain.model.NaturalPerson;
import local.project.Inzynierka.orchestration.errors.MappingException;
import local.project.Inzynierka.persistence.entity.NaturalPersonEntity;
import org.springframework.stereotype.Component;

@Component
public class NaturalPersonMapper {


    // TODO Add remaining field's mappings
    public NaturalPerson map(NaturalPersonEntity naturalPersonEntity){
        if(naturalPersonEntity == null ) {
            return null;  /// OPTIONAL, SO IT CAN BE NULL
        }
        NaturalPerson naturalPerson = new NaturalPerson();
        naturalPerson.setFirstName(naturalPersonEntity.getFirstName());
        naturalPerson.setId(naturalPersonEntity.getId());
        naturalPerson.setLastName(naturalPersonEntity.getLastName());

        return naturalPerson;
    }

    //TODO Add remaining field's mappings
    public NaturalPersonEntity map(NaturalPerson naturalPerson) {
        if(naturalPerson == null ) {
            return null; // OPTIONAL, SO IT CAN BE NULL
        }
        NaturalPersonEntity naturalPersonEntity = new NaturalPersonEntity();
        naturalPersonEntity.setFirstName(naturalPerson.getFirstName());
        naturalPersonEntity.setId(naturalPerson.getId());
        naturalPersonEntity.setLastName(naturalPerson.getLastName());

        return naturalPersonEntity;
    }

}
