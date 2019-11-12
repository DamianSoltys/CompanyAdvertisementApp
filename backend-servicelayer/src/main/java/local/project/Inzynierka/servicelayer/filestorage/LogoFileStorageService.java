package local.project.Inzynierka.servicelayer.filestorage;

import local.project.Inzynierka.servicelayer.company.CompanyLogoUUID;
import local.project.Inzynierka.servicelayer.company.event.BranchLogoAddedEvent;
import local.project.Inzynierka.servicelayer.company.event.CompanyLogoAddedEvent;
import local.project.Inzynierka.servicelayer.filestorage.validation.LogoUUIDValidator;
import local.project.Inzynierka.shared.utils.EntityName;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LogoFileStorageService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PhotoFileService photoFileService;
    private final LogoUUIDValidator logoUUIDValidator;

    public LogoFileStorageService(ApplicationEventPublisher applicationEventPublisher, PhotoFileService photoFileService, LogoUUIDValidator logoUUIDValidator) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.photoFileService = photoFileService;
        this.logoUUIDValidator = logoUUIDValidator;
    }

    public boolean saveCompanyLogo(CompanyLogoUUID companyLogoUUID, MultipartFile file) {

        if(logoUUIDValidator.validateCompanyLogoUUID(companyLogoUUID.getCompanyUUID(), companyLogoUUID.getLogoUUID())) {
            photoFileService.validateLogoFile(file);
            photoFileService.saveEntityLogo(companyLogoUUID.getCompanyUUID(), companyLogoUUID.getLogoUUID(), file, EntityName.COMPANY);
            applicationEventPublisher.publishEvent(new CompanyLogoAddedEvent(companyLogoUUID.getCompanyUUID()));
            return true;
        }
        return false;

    }

    public boolean saveBranchLogo(String branchUUID, String logoUUID, MultipartFile file) {
        if(logoUUIDValidator.validateBranchLogoUUID(branchUUID, logoUUID)) {
            photoFileService.validateLogoFile(file);
            photoFileService.saveEntityLogo(branchUUID, logoUUID, file, EntityName.BRANCH);
            applicationEventPublisher.publishEvent(new BranchLogoAddedEvent(branchUUID));
            return true;
        }
       return false;
    }

    public byte[] getCompanyLogo(String companyUUID, String logoUUID) {
        return photoFileService.getLogoFile(companyUUID, logoUUID, EntityName.COMPANY);
    }

    public byte[] getBranchLogo(String branchUUID, String logoUUID) {
        return photoFileService.getLogoFile(branchUUID, logoUUID, EntityName.BRANCH);
    }

}
