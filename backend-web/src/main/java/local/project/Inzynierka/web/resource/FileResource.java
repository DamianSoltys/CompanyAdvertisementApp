package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.company.BranchManagementPermissionService;
import local.project.Inzynierka.servicelayer.company.CompanyManagementPermissionService;
import local.project.Inzynierka.servicelayer.filestorage.LogoFileStorageService;
import local.project.Inzynierka.shared.UserAccount;
import local.project.Inzynierka.web.error.LogoKeyDoesNotMatchRequestParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.util.List;

@RestController
@RequestMapping(value = "/static")
@Slf4j
public class FileResource {

    private static final String LACK_OF_FILE_UPLOAD_PERMISSION = "User has no permission to upload logo";

    private final LogoFileStorageService logoFileStorageService;
    private final AuthFacade authFacade;
    private final CompanyManagementPermissionService companyManagementPermissionService;
    private final BranchManagementPermissionService branchManagementPermissionService;

    public FileResource(LogoFileStorageService logoFileStorageService, AuthFacade authFacade, CompanyManagementPermissionService companyManagementPermissionService, BranchManagementPermissionService branchManagementPermissionService) {
        this.logoFileStorageService = logoFileStorageService;
        this.authFacade = authFacade;
        this.companyManagementPermissionService = companyManagementPermissionService;
        this.branchManagementPermissionService = branchManagementPermissionService;
    }

    @PutMapping(value = "/company/{companyUUID}/{logoUUID}")
    public ResponseEntity<?> uploadCompanyLogo(@PathVariable(value = "companyUUID") String companyUUID,
                                               @PathVariable(value = "logoUUID") String logoUUID,
                                               StandardMultipartHttpServletRequest request) {

        UserAccount userAccount = authFacade.getAuthenticatedUser();
        if (!companyManagementPermissionService.hasManagingAuthority(companyUUID, userAccount)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(LACK_OF_FILE_UPLOAD_PERMISSION);
        }
        MultipartFile file = getLogoMultiPartFile(logoUUID, request);
        logoFileStorageService.saveCompanyLogo(companyUUID, logoUUID, file);

        return ResponseEntity.ok("OK");
    }

    private MultipartFile getLogoMultiPartFile(String logoUUID, StandardMultipartHttpServletRequest request) {
        MultiValueMap<String, MultipartFile> filesMap = request.getMultiFileMap();
        List<MultipartFile> file = filesMap.get(logoUUID);
        if (file == null) {
            throw new LogoKeyDoesNotMatchRequestParameterException();
        }
        return file.get(0);
    }

    @GetMapping(value = "/company/{companyUUID}/{logoUUID}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getCompanyLogo(@PathVariable(value = "companyUUID") String companyUUID,
                                 @PathVariable(value = "logoUUID") String logoUUID) {

        return logoFileStorageService.getCompanyLogo(companyUUID, logoUUID);
    }

    @PutMapping(value = "/branch/{branchUUID}/{logoUUID}")
    public ResponseEntity<?> uploadBranchLogo(@PathVariable(value = "branchUUID") String branchUUID,
                                              @PathVariable(value = "logoUUID") String logoUUID,
                                              StandardMultipartHttpServletRequest request) {


        UserAccount userAccount = authFacade.getAuthenticatedUser();
        if (!branchManagementPermissionService.hasManagingAuthority(branchUUID, userAccount)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(LACK_OF_FILE_UPLOAD_PERMISSION);
        }

        MultipartFile file = getLogoMultiPartFile(logoUUID, request);
        logoFileStorageService.saveBranchLogo(branchUUID, logoUUID, file);

        return ResponseEntity.ok("OK");
    }

    @GetMapping(value = "/branch/{branchUUID}/{logoUUID}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getBranchLogo(@PathVariable(value = "branchUUID") String branchUUID,
                                @PathVariable(value = "logoUUID") String logoUUID) {

        return logoFileStorageService.getBranchLogo(branchUUID, logoUUID);
    }
}
