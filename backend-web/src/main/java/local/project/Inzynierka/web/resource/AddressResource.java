package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.servicelayer.dto.Voivodeship;
import local.project.Inzynierka.servicelayer.errors.InvalidVoivodeshipException;
import local.project.Inzynierka.servicelayer.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class AddressResource {

    private final AddressService addressService;

    public AddressResource(AddressService addressService) {
        this.addressService = addressService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cities")
    public ResponseEntity<?> getCities(@RequestParam(value = "voivodeship") String value) {

        Voivodeship voivodeship = getVoivodeship(value);
        return ResponseEntity.ok(addressService.getCitiesByVoivodeship(voivodeship));
    }


    private Voivodeship getVoivodeship(@RequestParam("voivodeship") String voivodeship) {
        Voivodeship voivodeship1;
        try {
            voivodeship1 = Voivodeship.fromVoivodeship(voivodeship);
        } catch (IllegalArgumentException e) {
            throw new InvalidVoivodeshipException();
        }
        return voivodeship1;
    }
}
