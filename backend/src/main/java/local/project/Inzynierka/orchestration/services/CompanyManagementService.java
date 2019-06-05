package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;

import java.util.List;

public interface CompanyManagementService {
    void registerCompany(Company company, List<Branch> branches);
    Company getThroughBranch(Long id);
}
