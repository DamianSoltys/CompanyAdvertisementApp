package local.project.Inzynierka.web.mapper;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.web.dto.AddCompanyDto;
import local.project.Inzynierka.web.dto.CompanyBranchDto;

import java.util.ArrayList;
import java.util.List;

public class CompanyExtractor {

    private final Company company = new Company();
    private final List<Branch> branches;

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

        for(CompanyBranchDto companyBranchDto: addCompanyDto.getBranches()) {
            Branch branch = Branch.builder()
                    .address(Address.builder()
                            .city(companyBranchDto.getAddress().getCity())
                            .buildingNo(companyBranchDto.getAddress().getBuildingNo())
                            .street(companyBranchDto.getAddress().getStreet())
                            .voivodeship_id(new Voivoideship(companyBranchDto.getAddress().getVoivodeship().toString()))
                            .build()
                    )
                    .company(company)
                    .geoX(companyBranchDto.getGeoX())
                    .geoY(companyBranchDto.getGeoY())
                    .name(companyBranchDto.getName())
                    .build();

            branches.add(branch);
        }

    }
    private void checkForAddressesDuplicates(AddCompanyDto addCompanyDto) {

        long addressesNo = addCompanyDto.getBranches()
                .stream()
                .map(CompanyBranchDto::getAddress)
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
                .street(addCompanyDto.getAddress().getCity())
                .city(addCompanyDto.getAddress().getCity())
                .build());
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
        branch.getAddress().setCity(
                addCompanyDto.getBranches().get(0).getAddress().getCity() == null ?
                        addCompanyDto.getAddress().getCity():
                        addCompanyDto.getBranches().get(0).getAddress().getCity()
        );
        branch.getAddress().setVoivodeship_id(
                addCompanyDto.getBranches().get(0).getAddress().getVoivodeship() == null ?
                        new Voivoideship(addCompanyDto.getAddress().getVoivodeship().toString()) :
                        new Voivoideship(addCompanyDto.getBranches().get(0).getAddress().getVoivodeship().toString())
                );
        branch.getAddress().setBuildingNo(
                addCompanyDto.getBranches().get(0).getAddress().getBuildingNo() == null ?
                addCompanyDto.getAddress().getBuildingNo() :
                addCompanyDto.getBranches().get(0).getAddress().getBuildingNo()
        );
        branch.getAddress().setStreet(
                addCompanyDto.getBranches().get(0).getAddress().getStreet() == null ?
                        addCompanyDto.getAddress().getStreet() :
                        addCompanyDto.getBranches().get(0).getAddress().getStreet()
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

