package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.AddressRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.servicelayer.dto.Voivodeship;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AddressService {

    private final VoivodeshipRepository voivodeshipRepository;
    private final AddressRepository addressRepository;

    public AddressService(VoivodeshipRepository voivodeshipRepository, AddressRepository addressRepository) {
        this.voivodeshipRepository = voivodeshipRepository;
        this.addressRepository = addressRepository;
    }

    @Cacheable(value = "voivodeship", key = "#name")
    public Optional<Voivoideship> getVoivodeshipByName(String name) {

        return voivodeshipRepository.findByName(name);
    }

    public Set<String> getCitiesByVoivodeship(Voivodeship voivodeship) {
        return addressRepository.findByVoivodeship_id(voivodeship.toString())
                .stream()
                .map(Address::getCity)
                .collect(Collectors.toSet());
    }

    public Map<Voivodeship, Set<String>> getCitiesByVoivodeshipAggregate() {

        return Arrays.stream(Voivodeship.values())
                .collect(Collectors.toMap(
                        voivodeship -> voivodeship,
                        this::getCitiesByVoivodeship
                ));
    }
}
