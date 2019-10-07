package local.project.Inzynierka.servicelayer.dto.mapper;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.servicelayer.dto.CompanyBranchDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BranchMapper {

    public CompanyBranchDto map(Branch branch) {
        CompanyBranchDto companyBranchDto = new CompanyBranchDto();
        companyBranchDto.setAddress(new AddressMapper().map(branch.getAddress()));
        companyBranchDto.setGeoX(branch.getGeoX());
        companyBranchDto.setGeoY(branch.getGeoY());
        companyBranchDto.setName(branch.getName());
        return companyBranchDto;
    }

    public List<CompanyBranchDto> map(List<Branch> branches) {
        return branches.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
