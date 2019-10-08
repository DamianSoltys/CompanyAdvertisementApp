package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.servicelayer.dto.PersistedBranchDto;
import local.project.Inzynierka.servicelayer.services.BranchQueriesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class BranchResource {

    private final BranchQueriesService branchQueriesService;

    public BranchResource(BranchQueriesService branchQueriesService) {
        this.branchQueriesService = branchQueriesService;
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

}

