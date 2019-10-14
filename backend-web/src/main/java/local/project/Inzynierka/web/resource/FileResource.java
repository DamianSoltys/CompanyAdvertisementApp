package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.servicelayer.filestorage.FileStorageService;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.IOException;

@RestController
@RequestMapping(value = "/static")
@Slf4j
public class FileResource {

    private final FileStorageService fileStorageService;

    public FileResource(FileStorageService fileStorageService) {this.fileStorageService = fileStorageService;}

    @PutMapping(value = "/company/{companyUUID}/{logoUUID}")
    public String uploadCompanyLogo(@PathVariable(value = "companyUUID") String companyUUID,
                                    @PathVariable(value = "logoUUID") String logoUUID,
                                    StandardMultipartHttpServletRequest request) throws IOException {

        MultipartFile file = getLogoMultiPartFile(logoUUID, request);
        fileStorageService.saveCompanyLogo(companyUUID, logoUUID, file);

        return SimpleJsonFromStringCreator.toJson("OK");
    }

    private MultipartFile getLogoMultiPartFile(String logoUUID, StandardMultipartHttpServletRequest request) {
        MultiValueMap<String, MultipartFile> filesMap = request.getMultiFileMap();
        return filesMap.get(logoUUID.split("\\.")[0]).get(0);
    }

    @GetMapping(value = "/company/{companyUUID}/{logoUUID}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getCompanyLogo(@PathVariable(value = "companyUUID") String companyUUID,
                                 @PathVariable(value = "logoUUID") String logoUUID) throws IOException {

        return fileStorageService.getCompanyLogo(companyUUID, logoUUID);
    }

    @PutMapping(value = "/branch/{branchUUID}/{logoUUID}")
    public String uploadBranchLogo(@PathVariable(value = "branchUUID") String branchUUID,
                                   @PathVariable(value = "logoUUID") String logoUUID,
                                   StandardMultipartHttpServletRequest request) throws IOException {

        MultipartFile file = getLogoMultiPartFile(logoUUID, request);
        fileStorageService.saveBranchLogo(branchUUID, logoUUID, file);

        return SimpleJsonFromStringCreator.toJson("OK");
    }

    @GetMapping(value = "/branch/{branchUUID}/{logoUUID}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getBranchLogo(@PathVariable(value = "branchUUID") String branchUUID,
                                @PathVariable(value = "logoUUID") String logoUUID) throws IOException {

        return fileStorageService.getBranchLogo(branchUUID, logoUUID);
    }
}
