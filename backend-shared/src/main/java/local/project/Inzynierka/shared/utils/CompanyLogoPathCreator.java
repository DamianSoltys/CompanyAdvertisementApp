package local.project.Inzynierka.shared.utils;

import java.io.File;
import java.util.UUID;

public class CompanyLogoPathCreator {

    private static final String APPLICATION_URL = "http://localhost:8090/";

    private CompanyLogoPathCreator() {}

    ;

    public static String buildCompanyLogoURL() {
        String companyUUID = UUID.randomUUID().toString();
        String companyLogoUUID = UUID.randomUUID().toString();
        return String.format("%s%s", APPLICATION_URL,
                             getCompanyLogoFilePath(companyUUID, companyLogoUUID));
    }

    public static String getCompanyLogoDirectory(String companyUUID) {
        return String.format("static%scompany%s%s%s", File.separator, File.separator, companyUUID,
                             File.separator);
    }

    public static String getCompanyLogoFilePath(String companyUUID, String logoUUID) {
        return String.format("%s%s", getCompanyLogoDirectory(companyUUID), logoUUID);
    }
}
