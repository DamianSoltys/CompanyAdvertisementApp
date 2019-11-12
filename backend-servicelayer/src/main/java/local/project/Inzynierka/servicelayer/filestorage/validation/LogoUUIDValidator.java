package local.project.Inzynierka.servicelayer.filestorage.validation;

import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.shared.utils.FilePathCreator;
import org.springframework.stereotype.Component;

@Component
public class LogoUUIDValidator {

    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;

    public LogoUUIDValidator(CompanyRepository companyRepository, BranchRepository branchRepository) {
        this.companyRepository = companyRepository;
        this.branchRepository = branchRepository;
    }

    public boolean validateCompanyLogoUUID(String companyUUID, String logoUUID) {
        var company = companyRepository.findByCompanyUUID(companyUUID);
        if(company.isEmpty()) {
            return false;
        }

        return FilePathCreator.getFileKey(company.get().getLogoPath()).equals(logoUUID);
    }

    public boolean validateBranchLogoUUID(String branchUUID, String logoUUID) {
        var branch = branchRepository.findByBranchUUID(branchUUID);
        if(branch.isEmpty()) {
            return false;
        }

        return FilePathCreator.getFileKey(branch.get().getPhotoPath()).equals(logoUUID);
    }
}
