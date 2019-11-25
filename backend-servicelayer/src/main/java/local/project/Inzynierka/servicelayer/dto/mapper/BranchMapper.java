package local.project.Inzynierka.servicelayer.dto.mapper;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.servicelayer.dto.branch.AddBranchDto;
import local.project.Inzynierka.servicelayer.dto.branch.CompanyBranchDto;
import local.project.Inzynierka.servicelayer.dto.branch.PersistedBranchDto;
import local.project.Inzynierka.shared.utils.EntityName;
import local.project.Inzynierka.shared.utils.FilePathCreator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BranchMapper {

    private final CompanyRepository companyRepository;

    public BranchMapper(CompanyRepository companyRepository) {this.companyRepository = companyRepository;}

    public CompanyBranchDto mapInputBranch(Branch branch) {
        CompanyBranchDto companyBranchDto = new CompanyBranchDto();
        companyBranchDto.setAddress(new AddressMapper().map(branch.getAddress()));
        companyBranchDto.setGeoX(branch.getGeoX());
        companyBranchDto.setCategory(branch.getCompany().getCategory().getName());
        companyBranchDto.setGeoY(branch.getGeoY());
        companyBranchDto.setName(branch.getName());
        companyBranchDto.setGetLogoURL(branch.getPhotoPath());
        companyBranchDto.setPutLogoURL(FilePathCreator.getPutLogoURL(branch.getPhotoPath()));
        companyBranchDto.setHasLogoAdded(branch.isHasLogoAdded());
        companyBranchDto.setLogoKey(FilePathCreator.getFileKey(branch.getPhotoPath()));
        return companyBranchDto;
    }

    public List<CompanyBranchDto> mapInputBranch(List<Branch> branches) {
        return branches.stream()
                .map(this::mapInputBranch)
                .collect(Collectors.toList());
    }

    public PersistedBranchDto mapPersistedBranch(Branch branch) {

        Company company = companyRepository.findById(branch.getCompany().getId()).get();

        PersistedBranchDto persistedBranchDto = new PersistedBranchDto();
        persistedBranchDto.setAddress(new AddressMapper().map(branch.getAddress()));
        persistedBranchDto.setHasLogoAdded(branch.isHasLogoAdded());
        persistedBranchDto.setCompanyId(branch.getCompany().getId());
        persistedBranchDto.setCategory(company.getCategory().getName());
        persistedBranchDto.setGeoX(branch.getGeoX());
        persistedBranchDto.setGeoY(branch.getGeoY());
        persistedBranchDto.setName(branch.getName());
        persistedBranchDto.setBranchId(branch.getId());
        persistedBranchDto.setGetLogoURL(branch.getPhotoPath());
        persistedBranchDto.setPutLogoURL(FilePathCreator.getPutLogoURL(branch.getPhotoPath()));
        persistedBranchDto.setLogoKey(FilePathCreator.getFileKey(branch.getPhotoPath()));
        return persistedBranchDto;
    }

    public List<PersistedBranchDto> mapPersistedBranch(Iterable<Branch> branches) {
        return  StreamSupport.stream(branches.spliterator(), false)
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
                .photoPath(FilePathCreator.buildEntityLogoURL(entityUUID, EntityName.BRANCH))
                .build();
    }
}
