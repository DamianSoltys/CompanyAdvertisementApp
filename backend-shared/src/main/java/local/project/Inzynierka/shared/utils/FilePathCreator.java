package local.project.Inzynierka.shared.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static local.project.Inzynierka.shared.ApplicationConstants.APPLICATION_URL;
import static local.project.Inzynierka.shared.ApplicationConstants.URL_PATH_SEPARATOR;

public class FilePathCreator {

    private FilePathCreator() {}

    public static String buildEntityLogoURL(String entityUUID, EntityName entityName) {
        String logoUUID = UUID.randomUUID().toString();
        return String.format("%s%s", APPLICATION_URL,
                             getFileURLPath(entityUUID, logoUUID, entityName));
    }

    private static String getFileURLPath(String entityUUID, String logoUUID, EntityName entityName) {
        return String.format("%s%s", getEntityFileDirectoryURLPath(entityUUID, entityName), logoUUID);
    }

    private static String getEntityFileDirectoryURLPath(String entityUUID, EntityName entityName) {
        return String.format("static%s%s%s%s%s", URL_PATH_SEPARATOR, entityName.getEntityName(), URL_PATH_SEPARATOR, entityUUID,
                             URL_PATH_SEPARATOR);
    }

    public static String getFilePath(String entityUUID, String logoUUID, EntityName entityName) {
        String extension = ".png";
        return String.format("%s%s%s", getEntityFileDirectory(entityUUID, entityName), logoUUID, extension);
    }

    public static String getEntityFileDirectory(String entityUUID, EntityName entityName) {
        return String.format("static%s%s%s%s%s", File.separator, entityName.getEntityName(), File.separator, entityUUID,
                             File.separator);
    }

    public static String getFileKey(String logoPath) {
        if (logoPath != null) {
            final String URL_PATH_SEPARATOR = "/";
            List<String> backslashSplitPath = Arrays.asList(logoPath.split(URL_PATH_SEPARATOR));
            return backslashSplitPath.get(backslashSplitPath.size() - 1);
        }
        return "";
    }

    public static String getPutLogoURL(String logoPath) {
        if (logoPath != null) {
            final String URL_PATH_SEPARATOR = "/";
            List<String> backslashSplitPath = Arrays.asList(logoPath.split(URL_PATH_SEPARATOR));
            return String.join("/", backslashSplitPath.subList(0, backslashSplitPath.size() - 1));
        }
        return "";
    }
}
