package local.project.Inzynierka.shared.utils;

import java.io.File;
import java.util.UUID;

public class LogoFilePathCreator {

    private static final String APPLICATION_URL = "http://localhost:8090/";

    private LogoFilePathCreator() {}

    ;

    public static String buildEntityLogoURL(EntityName entityName) {
        String entityUUID = UUID.randomUUID().toString();
        String logoUUID = UUID.randomUUID().toString();
        return String.format("%s%s", APPLICATION_URL,
                             getLogoFilePath(entityUUID, logoUUID, entityName));
    }

    public static String getEntityLogoFileDirectory(String entityUUID, EntityName entityName) {
        return String.format("static%s%s%s%s%s", File.separator, entityName.getEntityName(), File.separator, entityUUID,
                             File.separator);
    }

    public static String getLogoFilePath(String entityUUID, String logoUUID, EntityName entityName) {
        String extension = ".png";
        return String.format("%s%s%s", getEntityLogoFileDirectory(entityUUID, entityName), logoUUID, extension);
    }
}
