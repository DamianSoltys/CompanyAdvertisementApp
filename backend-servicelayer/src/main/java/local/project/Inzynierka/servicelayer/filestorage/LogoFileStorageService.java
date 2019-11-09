package local.project.Inzynierka.servicelayer.filestorage;

import local.project.Inzynierka.servicelayer.company.event.BranchLogoAddedEvent;
import local.project.Inzynierka.servicelayer.company.event.CompanyLogoAddedEvent;
import local.project.Inzynierka.shared.utils.EntityName;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LogoFileStorageService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PhotoFileService photoFileService;

    public LogoFileStorageService(ApplicationEventPublisher applicationEventPublisher, PhotoFileService photoFileService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.photoFileService = photoFileService;
    }

    public void saveCompanyLogo(String companyUUID, String logoUUID, MultipartFile file) {

        photoFileService.validateLogoFile(file);
        photoFileService.saveEntityLogo(companyUUID, logoUUID, file, EntityName.COMPANY);
        applicationEventPublisher.publishEvent(new CompanyLogoAddedEvent(companyUUID));
    }

    public void saveBranchLogo(String branchUUID, String logoUUID, MultipartFile file) {
        photoFileService.validateLogoFile(file);
        photoFileService.saveEntityLogo(branchUUID, logoUUID, file, EntityName.BRANCH);
        applicationEventPublisher.publishEvent(new BranchLogoAddedEvent(branchUUID));
    }

    public byte[] getCompanyLogo(String companyUUID, String logoUUID) {
        return photoFileService.getLogoFile(companyUUID, logoUUID, EntityName.COMPANY);
    }

    public byte[] getBranchLogo(String branchUUID, String logoUUID) {
        return photoFileService.getLogoFile(branchUUID, logoUUID, EntityName.BRANCH);
    }

}
