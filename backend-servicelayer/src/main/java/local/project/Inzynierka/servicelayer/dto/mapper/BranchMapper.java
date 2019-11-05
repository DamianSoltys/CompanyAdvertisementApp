package local.project.Inzynierka.servicelayer.dto.mapper;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.servicelayer.dto.AddBranchDto;
import local.project.Inzynierka.servicelayer.dto.CompanyBranchDto;
import local.project.Inzynierka.servicelayer.dto.PersistedBranchDto;
import local.project.Inzynierka.shared.utils.EntityName;
import local.project.Inzynierka.shared.utils.LogoFilePathCreator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BranchMapper {

    public CompanyBranchDto mapInputBranch(Branch branch) {
        CompanyBranchDto companyBranchDto = new CompanyBranchDto();
        companyBranchDto.setAddress(new AddressMapper().map(branch.getAddress()));
        companyBranchDto.setGeoX(branch.getGeoX());
        companyBranchDto.setGeoY(branch.getGeoY());
        companyBranchDto.setName(branch.getName());
        companyBranchDto.setLogoURL(branch.getPhotoPath());
        companyBranchDto.setHasLogoAdded(branch.isHasLogoAdded());
        companyBranchDto.setLogoKey(LogoFilePathCreator.getLogoKey(branch.getPhotoPath()));
        return companyBranchDto;
    }

    public List<CompanyBranchDto> mapInputBranch(List<Branch> branches) {
        return branches.stream()
                .map(this::mapInputBranch)
                .collect(Collectors.toList());
    }

    public PersistedBranchDto mapPersistedBranch(Branch branch) {
        PersistedBranchDto persistedBranchDto = new PersistedBranchDto();
        persistedBranchDto.setAddress(new AddressMapper().map(branch.getAddress()));
        persistedBranchDto.setHasLogoAdded(branch.isHasLogoAdded());
        persistedBranchDto.setCompanyId(branch.getCompany().getId());
        persistedBranchDto.setGeoX(branch.getGeoX());
        persistedBranchDto.setGeoY(branch.getGeoY());
        persistedBranchDto.setName(branch.getName());
        persistedBranchDto.setBranchId(branch.getId());
        persistedBranchDto.setLogoPath(branch.getPhotoPath());
        persistedBranchDto.setLogoKey(LogoFilePathCreator.getLogoKey(branch.getPhotoPath()));
        return persistedBranchDto;
    }

    public List<PersistedBranchDto> mapPersistedBranch(Iterable<Branch> branches) {
        return StreamSupport.stream(branches.spliterator(), false)
                .map(this::mapPersistedBranch)
                .collect(Collectors.toList());
    }

    public Branch mapAddBranchDto(AddBranchDto addBranchDto, Long companyId, Long personId, Address address) {

        String entityUUID = UUID.randomUUID().toString();

        return Branch.builder()
                .name(addBranchDto.getName())
                .address(address)
                .hasLogoAdded(false)
                .company(Company.builder().id(companyId).build())
                .geoX(addBranchDto.getGeoX())
                .geoY(addBranchDto.getGeoY())
                .registerer(NaturalPerson.builder().id(personId).build())
                .branchUUID(entityUUID)
                .photoPath(LogoFilePathCreator.buildEntityLogoURL(entityUUID, EntityName.BRANCH))
                .build();
    }
}
