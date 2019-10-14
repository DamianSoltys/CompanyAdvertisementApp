package local.project.Inzynierka.servicelayer.filestorage;

import local.project.Inzynierka.shared.utils.CompanyLogoPathCreator;
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

        Files.createDirectories(Paths.get(CompanyLogoPathCreator.getCompanyLogoDirectory(companyUUID)));
        Files.write(Paths.get(CompanyLogoPathCreator.getCompanyLogoFilePath(companyUUID, logoUUID)),
                    file.getBytes());
    }

    private void validateLogoFile(MultipartFile file) {
        if (!MIME_IMAGE_JPEG.equals(file.getContentType()) && !MIME_IMAGE_PNG.equals(file.getContentType())) {
            throw new UnsupportedLogoFileFormat();
        }
    }
}
