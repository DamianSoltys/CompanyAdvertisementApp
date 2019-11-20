package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.company.CompanyManagementPermissionService;
import local.project.Inzynierka.servicelayer.social.SocialMediaConnectionService;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class SocialConnectionResource {

    private final SocialMediaConnectionService connectionService;
    private final AuthFacade authFacade;
    private final CompanyManagementPermissionService companyManagementPermissionService;

    public SocialConnectionResource(SocialMediaConnectionService connectionService, AuthFacade authFacade, CompanyManagementPermissionService companyManagementPermissionService) {
        this.connectionService = connectionService;
        this.authFacade = authFacade;
        this.companyManagementPermissionService = companyManagementPermissionService;
    }

    @GetMapping(value = "/social/connections/{companyId}")
    public ResponseEntity<?> getConnections(@PathVariable Long companyId) {

        UserAccount userAccount = authFacade.getAuthenticatedUser();
        if(companyManagementPermissionService.hasManagingAuthority(companyId,userAccount)) {
            return ResponseEntity.ok().body(connectionService.getSocialProfileConnections(companyId));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

    }
}
