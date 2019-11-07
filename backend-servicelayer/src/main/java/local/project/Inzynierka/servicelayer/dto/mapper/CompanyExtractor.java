package local.project.Inzynierka.servicelayer.dto.mapper;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.servicelayer.dto.branch.AddBranchDto;
import local.project.Inzynierka.servicelayer.dto.company.AddCompanyDto;

import java.util.ArrayList;
import java.util.List;

public class CompanyExtractor {

    private final Company company = new Company();
    private final List<Branch> branches;

    private AddressMapper addressMapper = new AddressMapper();

    public CompanyExtractor(AddCompanyDto addCompanyDto) {

        buildCompany(addCompanyDto);

        if(checkForLackOfBranches(addCompanyDto)){
            branches = null;
            return;
        }

        company.setHasBranch(true);
        branches = new ArrayList<>();


        if(addCompanyDto.getBranches().size() == 1 ) {
            buildSingleBranch(addCompanyDto);
            return;
        }

        checkForAddressesDuplicates(addCompanyDto);

        for (AddBranchDto addBranchDto : addCompanyDto.getBranches()) {
            Branch branch = Branch.builder()
                    .address(Address.builder()
                                     .city(addBranchDto.getAddress().getCity())
                                     .buildingNo(addBranchDto.getAddress().getBuildingNo())
                                     .street(addBranchDto.getAddress().getStreet())
                                     .apartmentNo(addBranchDto.getAddress().getApartmentNo())
                                     .voivodeship_id(new Voivoideship(addBranchDto.getAddress().getVoivodeship().toString()))
                            .build()
                    )
                    .company(company)
                    .geoX(addBranchDto.getGeoX())
                    .geoY(addBranchDto.getGeoY())
                    .name(addBranchDto.getName())
                    .build();

            branches.add(branch);
        }

    }
    private void checkForAddressesDuplicates(AddCompanyDto addCompanyDto) {

        long addressesNo = addCompanyDto.getBranches()
                .stream()
                .map(AddBranchDto::getAddress)
                .distinct()
                .count();

        if(addressesNo != addCompanyDto.getBranches().size()) {
            throw new IllegalStateException("Siedziby zakładów powinny być różne");
        }
    }

    private void buildCompany(AddCompanyDto addCompanyDto) {

        company.setAddress(Address.builder()
                .buildingNo(addCompanyDto.getAddress().getBuildingNo())
                .street(addCompanyDto.getAddress().getStreet())
                .voivodeship_id(new Voivoideship(addCompanyDto.getAddress().getVoivodeship().toString()))
                .apartmentNo(addCompanyDto.getAddress().getApartmentNo())
                .city(addCompanyDto.getAddress().getCity())
                .build());
        company.setCompanyWebsiteLink(addCompanyDto.getCompanyWebsiteUrl());
        company.setCategory(new Category(addCompanyDto.getCategory()));
        company.setDescription(addCompanyDto.getDescription());
        company.setName(addCompanyDto.getName());
        company.setNIP(addCompanyDto.getNIP());
        company.setREGON(addCompanyDto.getREGON());
    }

    private boolean checkForLackOfBranches(AddCompanyDto addCompanyDto) {
        if( addCompanyDto.getBranches() == null ) {
            company.setHasBranch(false);
            return true;
        }
        return false;
    }

    private void buildSingleBranch(AddCompanyDto addCompanyDto){
        Branch branch = new Branch();
        branch.setName(
                addCompanyDto.getBranches().get(0).getName() == null ?
                        addCompanyDto.getName() :
                        addCompanyDto.getBranches().get(0).getName()
        );
        branch.setAddress(
                addCompanyDto.getBranches().get(0).getAddress() == null ?
                        addressMapper.map(addCompanyDto.getAddress()):
                        addressMapper.map(addCompanyDto.getBranches().get(0).getAddress())
        );

        branch.setCompany(company);
        branch.setGeoX(addCompanyDto.getBranches().get(0).getGeoX());
        branch.setGeoY(addCompanyDto.getBranches().get(0).getGeoY());
        branches.add(branch);
    }


    public Company getCompany() {
        return company;
    }

    public List<Branch> getBranches() {
        return branches;
    }
}

