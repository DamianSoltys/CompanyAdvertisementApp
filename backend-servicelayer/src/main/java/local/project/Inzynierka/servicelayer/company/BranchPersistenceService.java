package local.project.Inzynierka.servicelayer.company;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.AddressRepository;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.servicelayer.dto.branch.AddBranchDto;
import local.project.Inzynierka.servicelayer.dto.branch.PersistedBranchDto;
import local.project.Inzynierka.servicelayer.dto.mapper.AddressMapper;
import local.project.Inzynierka.servicelayer.dto.mapper.BranchMapper;
import local.project.Inzynierka.servicelayer.errors.InvalidVoivodeshipException;
import local.project.Inzynierka.servicelayer.services.AddressService;
import local.project.Inzynierka.shared.utils.EntityName;
import local.project.Inzynierka.shared.utils.FilePathCreator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
class BranchPersistenceService {

    private final VoivodeshipRepository voivodeshipRepository;

    private final AddressRepository addressRepository;

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    private final AddressService addressService;

    BranchPersistenceService(VoivodeshipRepository voivodeshipRepository, AddressRepository addressRepository, BranchRepository branchRepository, BranchMapper branchMapper, AddressService addressService) {
        this.voivodeshipRepository = voivodeshipRepository;
        this.addressRepository = addressRepository;
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.addressService = addressService;
    }

    void buildAllCompanyBranches(List<Branch> branches, Company createdCompany) {
        branches.forEach(branch -> {
            branch.setCompany(createdCompany);
            branch.setHasLogoAdded(false);
            branch.setRegisterer(createdCompany.getRegisterer());
            String entityUUID = UUID.randomUUID().toString();
            branch.setPhotoPath(FilePathCreator.buildEntityLogoURL(entityUUID, EntityName.BRANCH));
            branch.setBranchUUID(entityUUID);
            Voivoideship branchVoivodeship = this.voivodeshipRepository.findByName(branch.getAddress().getVoivodeship_id().getName()).orElseThrow(InvalidVoivodeshipException::new);
            branch.getAddress().setVoivodeship_id(branchVoivodeship);
            Address branchAddress = this.addressRepository.save(branch.getAddress());
            branch.setAddress(branchAddress);
        });
    }

    Iterable<Branch> saveAll(List<Branch> branches) {
        return this.branchRepository.saveAll(branches);
    }

    List<Long> getCompanyBranchesIds(Long id) {
        return this.branchRepository.getAllByCompanyId(id);
    }

    Optional<Branch> getPersistedBranch(Long branchId) {
        return this.branchRepository.findById(branchId);
    }

    Optional<List<PersistedBranchDto>> saveBranch(List<AddBranchDto> branchDtos, Long companyId, Long personId) {

        List<Branch> branches = new ArrayList<>();
        branchDtos.stream().forEach(branch -> {
            Voivoideship voivoideship = addressService.getVoivodeshipByName(
                    branch.getAddress().getVoivodeship().toString())
                    .orElseThrow(InvalidVoivodeshipException::new);
            Address address = addressRepository.save(new AddressMapper().map(branch.getAddress(), voivoideship));
            Branch mappedBranch = branchMapper.mapAddBranchDto(branch, companyId, personId, address);
            branches.add(mappedBranch);
        });

        return Optional.of(this.branchRepository.saveAll(branches)).map(branchMapper::mapPersistedBranch);
    }
}
