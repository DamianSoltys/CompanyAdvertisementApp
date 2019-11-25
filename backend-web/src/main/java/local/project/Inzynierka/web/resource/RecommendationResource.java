package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.dto.recommendation.RecommendationDto;
import local.project.Inzynierka.servicelayer.services.RecommendationService;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationResource {

    private final RecommendationService recommendationService;
    private final AuthFacade authFacade;

    public RecommendationResource(RecommendationService recommendationService, AuthFacade authFacade) {
        this.recommendationService = recommendationService;
        this.authFacade = authFacade;
    }

    @GetMapping("")
    public ResponseEntity<List<RecommendationDto>> getRecommendations(
            @RequestParam(value = "category") List<String> categories) {

        UserAccount userAccount = authFacade.getAuthenticatedUser();
        return ResponseEntity.ok(recommendationService.getRecommendedBranches(categories, userAccount));
    }
}
