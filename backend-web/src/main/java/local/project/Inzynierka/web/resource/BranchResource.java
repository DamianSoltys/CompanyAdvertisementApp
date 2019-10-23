package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.company.BranchManagementPermissionService;
import local.project.Inzynierka.servicelayer.company.BranchManagementService;
import local.project.Inzynierka.servicelayer.dto.PersistedBranchDto;
import local.project.Inzynierka.servicelayer.dto.UpdateBranchInfoDto;
import local.project.Inzynierka.servicelayer.errors.BranchUpdateFailedException;
import local.project.Inzynierka.servicelayer.services.BranchQueriesService;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class BranchResource {

    private static final String LACK_OF_MANAGING_PERMISSION_MESSAGE = "Użytkownik nie ma pozwolenia na zarządzanie zakładem.";

    private final BranchQueriesService branchQueriesService;
    private final BranchManagementPermissionService branchManagementPermissionService;
    private final BranchManagementService branchManagementService;
    private final AuthFacade authFacade;

    public BranchResource(BranchQueriesService branchQueriesService, BranchManagementPermissionService branchManagementPermissionService, BranchManagementService branchManagementService, AuthFacade authFacade) {
        this.branchQueriesService = branchQueriesService;
        this.branchManagementPermissionService = branchManagementPermissionService;
        this.branchManagementService = branchManagementService;
        this.authFacade = authFacade;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/branch/{id}")
    public ResponseEntity<?> getBranch(@PathVariable(value = "id") Long branchId) {

        return branchQueriesService.getById(branchId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/branch")
    public Page<PersistedBranchDto> getBranches(Pageable pageable) {

        return branchQueriesService.getAll(pageable);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/branch/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable(value = "id") Long branchId, @RequestBody UpdateBranchInfoDto branchDto) {

        if (!branchManagementPermissionService.hasManagingAuthority(branchId, this.authFacade.getAuthenticatedUser())) {
            return new ResponseEntity<>(SimpleJsonFromStringCreator.toJson(LACK_OF_MANAGING_PERMISSION_MESSAGE), HttpStatus.FORBIDDEN);
        }

        return this.branchManagementService.updateBranchInfo(branchId, branchDto)
                .map(ResponseEntity::ok)
                .orElseThrow(BranchUpdateFailedException::new);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/branch/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable(value = "id") Long branchId) {

        branchManagementService.deleteBranch(branchId);

        return ResponseEntity.ok(SimpleJsonFromStringCreator.toJson(branchId.toString()));
    }

}

