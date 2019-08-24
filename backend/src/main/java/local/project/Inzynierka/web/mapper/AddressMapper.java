package local.project.Inzynierka.web.mapper;


import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.servicelayer.dto.Address;

public class AddressMapper {

    public local.project.Inzynierka.persistence.entity.Address map(Address address) {
        local.project.Inzynierka.persistence.entity.Address address1 = new local.project.Inzynierka.persistence.entity.Address();
        address1.setApartmentNo(address.getApartmentNo());
        address1.setBuildingNo(address.getBuildingNo());
        address1.setCity(address.getCity());
        address1.setStreet(address.getStreet());
        address1.setVoivodeship_id(new Voivoideship(address.getVoivodeship().toString()));

        return address1;
    }
}
