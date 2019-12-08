package local.project.Inzynierka.web.resource;


import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.company.FavouriteBranchesService;
import local.project.Inzynierka.servicelayer.dto.branch.FavouriteBranchGetDto;
import local.project.Inzynierka.servicelayer.dto.branch.FavouriteBranchPostDto;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/favourite")
public class FavouritesBranchesResource {

    private final FavouriteBranchesService favouriteBranchesService;
    private final AuthFacade authFacade;

    public FavouritesBranchesResource(FavouriteBranchesService favouriteBranchesService, AuthFacade authFacade) {
        this.favouriteBranchesService = favouriteBranchesService;
        this.authFacade = authFacade;
    }


    @PostMapping(value = "")
    public ResponseEntity<FavouriteBranchGetDto> addFavouriteBranch(@RequestBody @Valid FavouriteBranchPostDto favouriteBranchPostDto) {

        if (authFacade.hasPrincipalHavePermissionToUserResource(favouriteBranchPostDto.getUserId())) {
            return ResponseEntity.ok(favouriteBranchesService.addFavoriteBranch(favouriteBranchPostDto));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @GetMapping(value = "/{favouriteBranchUuid}")
    public ResponseEntity<FavouriteBranchGetDto> getFavouriteBranch(@PathVariable String favouriteBranchUuid) {
        return ResponseEntity.ok(favouriteBranchesService.getFavouriteBranch(favouriteBranchUuid).orElse(null));
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<FavouriteBranchGetDto>> getUsersFavourites(@PathVariable Long userId) {
        return ResponseEntity.ok(favouriteBranchesService.getFavouritesByUser(userId));
    }

    @DeleteMapping(value = "/{favouriteBranchUuid}")
    public ResponseEntity<Boolean> deleteFavouriteBranch(@PathVariable String favouriteBranchUuid){

        UserAccount userAccount = authFacade.getAuthenticatedUser();
        return ResponseEntity.ok(favouriteBranchesService.deleteFavouriteBranch(favouriteBranchUuid, userAccount.getId()));
    }
}
