package local.project.Inzynierka.servicelayer.company;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.servicelayer.company.event.AllBranchesHasBeenDeletedEvent;
import local.project.Inzynierka.servicelayer.company.event.BranchLogoAddedEvent;
import local.project.Inzynierka.servicelayer.dto.CompanyBranchDto;
import local.project.Inzynierka.servicelayer.dto.UpdateBranchInfoDto;
import local.project.Inzynierka.servicelayer.dto.mapper.AddressMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BranchManagementService {

    private final BranchPersistenceService branchPersistenceService;
    private final BranchRepository branchRepository;
    private final VoivodeshipRepository voivodeshipRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public BranchManagementService(BranchPersistenceService branchPersistenceService, BranchRepository branchRepository, VoivodeshipRepository voivodeshipRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.branchPersistenceService = branchPersistenceService;
        this.branchRepository = branchRepository;
        this.voivodeshipRepository = voivodeshipRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public Optional<CompanyBranchDto> updateBranchInfo(Long id, UpdateBranchInfoDto updateBranchInfoDto) {

        Optional<Branch> optionalBranch = this.branchPersistenceService.getPersistedBranch(id);

        return optionalBranch
                .map(branch -> updateBranch(updateBranchInfoDto, branch))
                .map(this::buildBranchInfoDto);
    }

    private CompanyBranchDto buildBranchInfoDto(Branch branch) {
        AddressMapper addressMapper = new AddressMapper();
        return CompanyBranchDto.builder()
                .hasLogoAdded(branch.isHasLogoAdded())
                .address(addressMapper.map(branch.getAddress()))
                .geoX(branch.getGeoX())
                .geoY(branch.getGeoY())
                .name(branch.getName())
                .build();
    }

    private Branch updateBranch(UpdateBranchInfoDto updateBranchInfoDto, Branch branch) {

        Address address = branch.getAddress();
        if (updateBranchInfoDto.getAddress() != null) {
            Voivoideship voivoideship = voivodeshipRepository
                    .findByName(updateBranchInfoDto.getAddress().getVoivodeship().name())
                    .orElseThrow(IllegalArgumentException::new);

            address.setVoivodeship_id(updateBranchInfoDto.getAddress().getVoivodeship() == null ?
                                              voivoideship :
                                              branch.getAddress().getVoivodeship_id());
            address.setApartmentNo(updateBranchInfoDto.getAddress().getApartmentNo() == null ?
                                           branch.getAddress().getApartmentNo() :
                                           updateBranchInfoDto.getAddress().getApartmentNo()
            );

            address.setStreet(updateBranchInfoDto.getAddress().getStreet() == null ?
                                      branch.getAddress().getStreet() :
                                      updateBranchInfoDto.getAddress().getStreet()
            );

            address.setCity(updateBranchInfoDto.getAddress().getCity() == null ?
                                    branch.getAddress().getCity() :
                                    updateBranchInfoDto.getAddress().getCity()
            );

            address.setBuildingNo(updateBranchInfoDto.getAddress().getBuildingNo() == null ?
                                          branch.getAddress().getBuildingNo() :
                                          updateBranchInfoDto.getAddress().getBuildingNo()
            );
        }
        branch.setAddress(address);

        branch.setGeoX(
                updateBranchInfoDto.getGeoX() == null ? branch.getGeoX() :
                        updateBranchInfoDto.getGeoX()
        );
        branch.setGeoY(
                updateBranchInfoDto.getGeoY() == null ? branch.getGeoY() :
                        updateBranchInfoDto.getGeoY()
        );
        branch.setName(
                updateBranchInfoDto.getName() == null ? branch.getName() :
                        updateBranchInfoDto.getName()
        );
        return this.branchRepository.save(branch);
    }

    public void deleteBranch(Long branchId) {

        Branch branch = branchRepository.findById(branchId).orElseThrow(IllegalStateException::new);
        Boolean companyHasBranchesApartFromDeletedOne = branchRepository.findByCompany(branch.getCompany())
                .stream()
                .anyMatch(branch1 -> !branch1.equals(branch));
        if (!companyHasBranchesApartFromDeletedOne) {
            applicationEventPublisher.publishEvent(new AllBranchesHasBeenDeletedEvent(branch.getCompany().getId()));
        }

        this.branchRepository.deleteById(branchId);

    }

    @Async
    @EventListener
    public void setBranchHasLogoFlag(BranchLogoAddedEvent branchLogoAddedEvent) {
        Branch branch = branchRepository.findByBranchUUID(branchLogoAddedEvent.getBranchUUID())
                .orElseThrow(IllegalStateException::new);
        branch.setHasLogoAdded(true);
        branchRepository.save(branch);
    }

}
