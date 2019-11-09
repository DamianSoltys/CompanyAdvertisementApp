package local.project.Inzynierka.servicelayer.filestorage;

import local.project.Inzynierka.shared.utils.EntityName;
import local.project.Inzynierka.shared.utils.FilePathCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class PhotoFileService {

    private static final String MIME_IMAGE_JPEG = "image/jpeg";
    private static final String MIME_IMAGE_PNG = "image/png";

    public void saveEntityLogo(String entityUUID, String logoUUID, MultipartFile file, EntityName entityName) {

        try {
            Files.createDirectories(Paths.get(FilePathCreator.getEntityFileDirectory(entityUUID, entityName)));
        } catch (IOException e) {
            throw new FailedToCreateDirectoryException(e.getMessage(), e);
        }
        try {
            Files.write(Paths.get(FilePathCreator.getFilePath(entityUUID, logoUUID, entityName)),
                        file.getBytes());
        } catch (IOException e) {
            throw new FailedToSaveLogoException(e.getMessage(), e);
        }

    }

    public byte[] getLogoFile(String entityUUID, String logoUUID, EntityName entityName) {
        try {
            return Files.readAllBytes(Paths.get(
                    FilePathCreator.getFilePath(entityUUID, logoUUID, entityName)));
        } catch (IOException e) {
            throw new LackOfLogoException();
        }
    }

    public void validateLogoFile(MultipartFile file) {
        if (!MIME_IMAGE_JPEG.equals(file.getContentType()) && !MIME_IMAGE_PNG.equals(file.getContentType())) {
            throw new UnsupportedLogoFileFormat();
        }
    }
}
