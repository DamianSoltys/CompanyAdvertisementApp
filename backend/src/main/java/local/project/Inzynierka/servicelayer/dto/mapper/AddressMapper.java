package local.project.Inzynierka.servicelayer.dto.mapper;


import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.servicelayer.dto.Address;
import local.project.Inzynierka.servicelayer.dto.Voivodeship;

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

    public Address map(local.project.Inzynierka.persistence.entity.Address addressEntity) {
        Address address = new Address();
        address.setVoivodeship(Voivodeship.fromVoivodeship(addressEntity.getVoivodeship_id().getName()));
        address.setStreet(addressEntity.getStreet());
        address.setCity(addressEntity.getCity());
        address.setBuildingNo(addressEntity.getBuildingNo());
        address.setApartmentNo(addressEntity.getApartmentNo());

        return address;
    }
}
