package local.project.Inzynierka.shared.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LogoFilePathCreator {

    private static final String APPLICATION_URL = "http://localhost:8090/";
    private static final String URL_PATH_SEPARATOR = "/";

    private LogoFilePathCreator() {}

    public static String buildEntityLogoURL(String entityUUID, EntityName entityName) {
        String logoUUID = UUID.randomUUID().toString();
        return String.format("%s%s", APPLICATION_URL,
                             getLogoFileURLPath(entityUUID, logoUUID, entityName));
    }

    public static String getEntityLogoFileDirectory(String entityUUID, EntityName entityName) {
        return String.format("static%s%s%s%s%s", File.separator, entityName.getEntityName(), File.separator, entityUUID,
                             File.separator);
    }

    private static String getEntityLogoFileDirectoryURLPath(String entityUUID, EntityName entityName) {
        return String.format("static%s%s%s%s%s", URL_PATH_SEPARATOR, entityName.getEntityName(), URL_PATH_SEPARATOR, entityUUID,
                             URL_PATH_SEPARATOR);
    }

    public static String getLogoFilePath(String entityUUID, String logoUUID, EntityName entityName) {
        String extension = ".png";
        return String.format("%s%s%s", getEntityLogoFileDirectory(entityUUID, entityName), logoUUID, extension);
    }

    private static String getLogoFileURLPath(String entityUUID, String logoUUID, EntityName entityName) {
        return String.format("%s%s", getEntityLogoFileDirectoryURLPath(entityUUID, entityName), logoUUID);
    }

    public static String getLogoKey(String logoPath) {

        if (logoPath != null) {
            final String URL_PATH_SEPARATOR = "/";
            List<String> backslashSplitPath = Arrays.asList(logoPath.split(URL_PATH_SEPARATOR));
            return backslashSplitPath.get(backslashSplitPath.size() - 1);
        }
        return "";

    }
}
