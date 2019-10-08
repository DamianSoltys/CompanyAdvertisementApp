package local.project.Inzynierka.servicelayer.dto.mapper;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.servicelayer.dto.CompanyBranchDto;
import local.project.Inzynierka.servicelayer.dto.PersistedBranchDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BranchMapper {

    public CompanyBranchDto mapInputBranch(Branch branch) {
        CompanyBranchDto companyBranchDto = new CompanyBranchDto();
        companyBranchDto.setAddress(new AddressMapper().map(branch.getAddress()));
        companyBranchDto.setGeoX(branch.getGeoX());
        companyBranchDto.setGeoY(branch.getGeoY());
        companyBranchDto.setName(branch.getName());
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
        persistedBranchDto.setGeoX(branch.getGeoX());
        persistedBranchDto.setGeoY(branch.getGeoY());
        persistedBranchDto.setName(branch.getName());
        persistedBranchDto.setBranchId(branch.getId());
        return persistedBranchDto;
    }

    public List<PersistedBranchDto> mapPersistedBranch(List<Branch> branches) {
        return branches.stream()
                .map(this::mapPersistedBranch)
                .collect(Collectors.toList());
    }
}
