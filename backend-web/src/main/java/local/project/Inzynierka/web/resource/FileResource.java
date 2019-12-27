package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.company.BranchManagementPermissionService;
import local.project.Inzynierka.servicelayer.company.CompanyLogoUUID;
import local.project.Inzynierka.servicelayer.company.CompanyManagementPermissionService;
import local.project.Inzynierka.servicelayer.filestorage.LogoFileStorageService;
import local.project.Inzynierka.servicelayer.filestorage.PromotionItemPhotoService;
import local.project.Inzynierka.shared.UserAccount;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/static")
@Slf4j
public class FileResource {

    private static final String LOGO_UUID_REQUIRED_MESSAGE = "You have to provide logo's UUID as a key in formData body.";
    private static final String INCORRECT_LOGO_UUID_MESSAGE = "You have provided incorrect logoUUID.";
    private static final String LACK_OF_FILE_UPLOAD_PERMISSION = "User has no permission to upload logo";

    private final LogoFileStorageService logoFileStorageService;
    private final PromotionItemPhotoService promotionItemPhotoService;
    private final AuthFacade authFacade;
    private final CompanyManagementPermissionService companyManagementPermissionService;
    private final BranchManagementPermissionService branchManagementPermissionService;

    public FileResource(LogoFileStorageService logoFileStorageService, PromotionItemPhotoService promotionItemPhotoService, AuthFacade authFacade, CompanyManagementPermissionService companyManagementPermissionService, BranchManagementPermissionService branchManagementPermissionService) {
        this.logoFileStorageService = logoFileStorageService;
        this.promotionItemPhotoService = promotionItemPhotoService;
        this.authFacade = authFacade;
        this.companyManagementPermissionService = companyManagementPermissionService;
        this.branchManagementPermissionService = branchManagementPermissionService;
    }

    @PutMapping(value = "/company/{companyUUID}")
    public ResponseEntity<?> uploadCompanyLogo(@PathVariable(value = "companyUUID") String companyUUID,
                                               StandardMultipartHttpServletRequest request) {

        UserAccount userAccount = authFacade.getAuthenticatedUser();
        if (!companyManagementPermissionService.hasManagingAuthority(companyUUID, userAccount)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(LACK_OF_FILE_UPLOAD_PERMISSION);
        }
        Map<String, MultipartFile> filesMap = getUUIDToMultiPartFileMapping(request);

        var filesMapIterator = filesMap.entrySet().iterator();
        if (filesMapIterator.hasNext()) {
            Map.Entry<String, MultipartFile> entry = filesMap.entrySet().iterator().next();
            String logoUUID = entry.getKey();
            MultipartFile file = entry.getValue();

            var logoID = CompanyLogoUUID.builder()
                    .logoUUID(logoUUID)
                    .companyUUID(companyUUID)
                    .build();
            if (logoFileStorageService.saveCompanyLogo(logoID, file)) {
                return ResponseEntity.ok(SimpleJsonFromStringCreator.toJson("OK"));
            }

            return ResponseEntity.badRequest().body(SimpleJsonFromStringCreator.toJson(INCORRECT_LOGO_UUID_MESSAGE));

        }

        return ResponseEntity.badRequest().body(SimpleJsonFromStringCreator.toJson(LOGO_UUID_REQUIRED_MESSAGE));
    }

    private Map<String, MultipartFile> getUUIDToMultiPartFileMapping(StandardMultipartHttpServletRequest request) {
        MultiValueMap<String, MultipartFile> filesMap = request.getMultiFileMap();
        List<String> photoUUIDs = Collections.list(request.getParameterNames());

        return photoUUIDs.stream()
                .collect(Collectors.toMap(Function.identity(), uuid ->
                        filesMap.get(uuid)
                                .stream()
                                .findFirst()
                                .orElseThrow(LogoKeyDoesNotMatchRequestParameterException::new)));
    }

    @GetMapping(value = "/company/{companyUUID}/{logoUUID}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getCompanyLogo(@PathVariable(value = "companyUUID") String companyUUID,
                                 @PathVariable(value = "logoUUID") String logoUUID) {

        return logoFileStorageService.getCompanyLogo(companyUUID, logoUUID);
    }

    @PutMapping(value = "/branch/{branchUUID}")
    public ResponseEntity<?> uploadBranchLogo(@PathVariable(value = "branchUUID") String branchUUID,
                                              StandardMultipartHttpServletRequest request) {


        UserAccount userAccount = authFacade.getAuthenticatedUser();
        if (!branchManagementPermissionService.hasManagingAuthority(branchUUID, userAccount)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(LACK_OF_FILE_UPLOAD_PERMISSION);
        }

        Map<String, MultipartFile> filesMap = getUUIDToMultiPartFileMapping(request);

        var filesMapIterator = filesMap.entrySet().iterator();
        if (filesMapIterator.hasNext()) {
            Map.Entry<String, MultipartFile> entry = filesMap.entrySet().iterator().next();
            String logoUUID = entry.getKey();
            MultipartFile file = entry.getValue();

            if (logoFileStorageService.saveBranchLogo(branchUUID, logoUUID, file)) {
                return ResponseEntity.ok(SimpleJsonFromStringCreator.toJson("OK"));
            }

            return ResponseEntity.badRequest().body(SimpleJsonFromStringCreator.toJson(INCORRECT_LOGO_UUID_MESSAGE));
        }

        return ResponseEntity.badRequest().body(SimpleJsonFromStringCreator.toJson(LOGO_UUID_REQUIRED_MESSAGE));

    }

    @GetMapping(value = "/branch/{branchUUID}/{logoUUID}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getBranchLogo(@PathVariable(value = "branchUUID") String branchUUID,
                                @PathVariable(value = "logoUUID") String logoUUID) {

        return logoFileStorageService.getBranchLogo(branchUUID, logoUUID);
    }

    @PutMapping(value = "/pi/{promotionItemUUID}")
    public ResponseEntity<?> uploadPromotionItemPhotos(@PathVariable(value = "promotionItemUUID") String promotionItemUUID,
                                                       StandardMultipartHttpServletRequest request) {


        var filesMapping = getUUIDToMultiPartFileMapping(request);
        promotionItemPhotoService.savePhotos(promotionItemUUID, filesMapping);

        return ResponseEntity.ok(SimpleJsonFromStringCreator.toJson("OK"));
    }
}
