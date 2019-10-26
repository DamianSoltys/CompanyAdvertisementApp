package local.project.Inzynierka.servicelayer.filestorage;

import local.project.Inzynierka.servicelayer.company.event.BranchLogoAddedEvent;
import local.project.Inzynierka.servicelayer.company.event.CompanyLogoAddedEvent;
import local.project.Inzynierka.shared.utils.EntityName;
import local.project.Inzynierka.shared.utils.LogoFilePathCreator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class LogoFileStorageService {

    private static final String MIME_IMAGE_JPEG = "image/jpeg";
    private static final String MIME_IMAGE_PNG = "image/png";

    private final ApplicationEventPublisher applicationEventPublisher;

    public LogoFileStorageService(ApplicationEventPublisher applicationEventPublisher) {this.applicationEventPublisher = applicationEventPublisher;}

    public void saveCompanyLogo(String companyUUID, String logoUUID, MultipartFile file) {

        validateLogoFile(file);
        saveEntityLogo(companyUUID, logoUUID, file, EntityName.COMPANY);
        applicationEventPublisher.publishEvent(new CompanyLogoAddedEvent(companyUUID));
    }

    public void saveBranchLogo(String branchUUID, String logoUUID, MultipartFile file) {
        validateLogoFile(file);
        saveEntityLogo(branchUUID, logoUUID, file, EntityName.BRANCH);
        applicationEventPublisher.publishEvent(new BranchLogoAddedEvent(branchUUID));
    }

    private void saveEntityLogo(String entityUUID, String logoUUID, MultipartFile file, EntityName entityName) {

        try {
            Files.createDirectories(Paths.get(LogoFilePathCreator.getEntityLogoFileDirectory(entityUUID, entityName)));
        } catch (IOException e) {
            throw new FailedToCreateDirectoryException(e.getMessage(), e);
        }
        try {
            Files.write(Paths.get(LogoFilePathCreator.getLogoFilePath(entityUUID, logoUUID, entityName)),
                        file.getBytes());
        } catch (IOException e) {
            throw new FailedToSaveLogoException(e.getMessage(), e);
        }

    }

    private void validateLogoFile(MultipartFile file) {
        if (!MIME_IMAGE_JPEG.equals(file.getContentType()) && !MIME_IMAGE_PNG.equals(file.getContentType())) {
            throw new UnsupportedLogoFileFormat();
        }
    }

    public byte[] getCompanyLogo(String companyUUID, String logoUUID) {
        return getLogoFile(companyUUID, logoUUID, EntityName.COMPANY);
    }

    public byte[] getBranchLogo(String branchUUID, String logoUUID) {
        return getLogoFile(branchUUID, logoUUID, EntityName.BRANCH);
    }

    private byte[] getLogoFile(String entityUUID, String logoUUID, EntityName entityName) {
        try {
            return Files.readAllBytes(Paths.get(
                    LogoFilePathCreator.getLogoFilePath(entityUUID, logoUUID, entityName)));
        } catch (IOException e) {
            throw new LackOfLogoException();
        }
    }
}
