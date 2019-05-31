package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.*;
import local.project.Inzynierka.persistence.repository.*;
import local.project.Inzynierka.shared.AuthenticationFacade;
import local.project.Inzynierka.shared.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CompanyManagementServiceImpl implements CompanyManagementService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VoivodeshipRepository voivodeshipRepository;


    @Override @Transactional
    public void registerCompany(Company company, List<Branch> branches) {
        User user = userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());
        NaturalPerson naturalPerson = user.getNaturalPerson();

        Timestamp now = DateUtils.getNowTimestamp();

        Category soughtCategory = categoryRepository.findByName(company.getCategory().getName());
        if(soughtCategory == null ) {
            soughtCategory = company.getCategory();
            soughtCategory.setCreatedAt(now);
            soughtCategory.setName(company.getCategory().getName());
            soughtCategory.setId(0L);
        }

        soughtCategory.setModifiedAt(now);
        Category category = categoryRepository.save(soughtCategory);

        Voivoideship voivoideship = voivodeshipRepository.findByName(company.getVoivodeship_id().getName());

        Company companyInCreation = Company.builder()
                .buildingNo(company.getBuildingNo())
                .category(category)
                .city(company.getCity())
                .createdAt(now)
                .modifiedAt(now)
                .description(company.getDescription())
                .hasBranch(company.isHasBranch())
                .id(0L)
                .name(company.getName())
                .NIP(company.getNIP())
                .registerer(naturalPerson)
                .photoPath(company.getPhotoPath())
                .voivodeship_id(voivoideship)
                .street(company.getStreet())
                .REGON(company.getREGON())
                .build();

        Company createdCompany = companyRepository.save(companyInCreation);

        if( !companyInCreation.isHasBranch()) {
            return;
        }

        for(int i=0; i< branches.size();i++) {
            Branch branch = branches.get(i);
            branch.setCreatedAt(now);
            branch.setModifiedAt(now);
            branch.setCompany(createdCompany);
            branch.setRegisterer(naturalPerson);
            branch.setId(0L);
            Voivoideship branchVoivodeship = voivodeshipRepository.findByName(branch.getVoivodeship_id().getName());
            branch.setVoivodeship_id(branchVoivodeship);

            branches.set(i, branch);
        }

        branchRepository.saveAll(branches);
    }
}
