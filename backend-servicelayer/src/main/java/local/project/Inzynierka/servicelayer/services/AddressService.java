package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    private final VoivodeshipRepository voivodeshipRepository;

    public AddressService(VoivodeshipRepository voivodeshipRepository) {
        this.voivodeshipRepository = voivodeshipRepository;
    }

    @Cacheable(value = "voivodeship", key = "#name")
    public Optional<Voivoideship> getVoivodeshipByName(String name) {

        return voivodeshipRepository.findByName(name);
    }
}
