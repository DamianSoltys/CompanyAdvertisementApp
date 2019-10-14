package local.project.Inzynierka.servicelayer.filestorage;

import local.project.Inzynierka.shared.utils.EntityName;
import local.project.Inzynierka.shared.utils.LogoFilePathCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private static final String MIME_IMAGE_JPEG = "image/jpeg";
    private static final String MIME_IMAGE_PNG = "image/png";

    public void saveCompanyLogo(String companyUUID, String logoUUID, MultipartFile file) throws IOException {

        validateLogoFile(file);

        Files.createDirectories(Paths.get(LogoFilePathCreator.getEntityLogoFileDirectory(companyUUID, EntityName.COMPANY)));
        Files.write(Paths.get(LogoFilePathCreator.getLogoFilePath(companyUUID, logoUUID, EntityName.COMPANY)),
                    file.getBytes());
    }

    private void validateLogoFile(MultipartFile file) {
        if (!MIME_IMAGE_JPEG.equals(file.getContentType()) && !MIME_IMAGE_PNG.equals(file.getContentType())) {
            throw new UnsupportedLogoFileFormat();
        }
    }

    public byte[] getCompanyLogo(String companyUUID, String logoUUID) throws IOException {
        return Files.readAllBytes(Paths.get(
                LogoFilePathCreator.getLogoFilePath(companyUUID, logoUUID, EntityName.COMPANY)));
    }
}
