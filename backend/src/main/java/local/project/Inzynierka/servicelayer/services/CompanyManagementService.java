package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.servicelayer.dto.AddCompanyDto;
import local.project.Inzynierka.web.mapper.CompanyExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyManagementService {

    private final CompanyRepository companyRepository;

    private final CompanyPersistenceService companyPersistenceService;

    private final BranchPersistenceService branchPersistenceService;

    public CompanyManagementService(CompanyRepository companyRepository, CompanyPersistenceService companyPersistenceService, BranchPersistenceService branchPersistenceService) {
        this.companyRepository = companyRepository;
        this.companyPersistenceService = companyPersistenceService;
        this.branchPersistenceService = branchPersistenceService;
    }

    @Transactional
    public void registerCompany(AddCompanyDto addCompanyDto) {

        CompanyExtractor companyExtractor = new CompanyExtractor(addCompanyDto);
        List<Branch> branches = companyExtractor.getBranches();
        Company company = companyExtractor.getCompany();

        Company createdCompany = this.companyPersistenceService.getPersistedCompany(company);

        if (!createdCompany.hasBranch()) {
            return;
        }

        this.branchPersistenceService.buildAllCompanyBranches(branches, createdCompany);

        this.branchPersistenceService.saveAll(branches);
    }

    public boolean companyExists(Long id) {
        return this.companyRepository.getByCompanyId(id) != null;
    }
}
