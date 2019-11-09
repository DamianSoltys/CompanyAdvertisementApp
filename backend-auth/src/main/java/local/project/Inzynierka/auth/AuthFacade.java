package local.project.Inzynierka.auth;

import local.project.Inzynierka.shared.UserAccount;
import org.springframework.security.core.Authentication;


//BOTH AUTHENTICATION AND AUTHORIZATION - WILL THINK ABOUT SEPARATING IT
public interface AuthFacade {
    //AUTHENTICATION
    Authentication getAuthentication();

    UserAccount getAuthenticatedUser();

    //AUTHORIZATION
    boolean hasPrincipalHavePermissionToUserResource(Long userId);

    boolean hasPrincipalHavePermissionToNaturalPersonResource(Long userId, Long personId);

    boolean hasPrincipalHavePermissionToCommentResource(Long commentId);

    boolean hasPrincipalHavePermissionToRatingResource(Long ratingId);

    boolean hasPrincipalHavePermissionToPromotionItemResource(String promotionItemUUID);

}
