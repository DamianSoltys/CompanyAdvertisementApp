package local.project.Inzynierka.auth;

import local.project.Inzynierka.persistence.entity.Comment;
import local.project.Inzynierka.persistence.entity.PromotionItem;
import local.project.Inzynierka.persistence.entity.Rating;
import local.project.Inzynierka.persistence.repository.CommentRepository;
import local.project.Inzynierka.persistence.repository.PromotionItemRepository;
import local.project.Inzynierka.persistence.repository.RatingRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthFacadeImpl implements AuthFacade {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;
    private final PromotionItemRepository promotionItemRepository;

    public AuthFacadeImpl(UserRepository userRepository, CommentRepository commentRepository,
                          RatingRepository ratingRepository, PromotionItemRepository promotionItemRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.ratingRepository = ratingRepository;
        this.promotionItemRepository = promotionItemRepository;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserAccount getAuthenticatedUser() {
        return this.userRepository.getByAddressEmail(this.getAuthentication().getName());
    }

    @Override
    public boolean hasPrincipalHavePermissionToUserResource(Long userId) {
        return Optional.ofNullable(this.getAuthenticatedUser())
                .map(user -> user.getId().equals(userId))
                .orElse(false);
    }

    @Override
    public boolean hasPrincipalHavePermissionToNaturalPersonResource(Long userId, Long personId) {
        return Optional.ofNullable(this.getAuthenticatedUser())
                .map(user -> user.getId().equals(userId) &&
                        user.isNaturalPersonRegistered() &&
                        user.personId().equals(personId))
                .orElse(false);
    }

    @Override
    public boolean hasPrincipalHavePermissionToCommentResource(Long commentId) {
        return this.commentRepository.findById(commentId)
                .map(Comment::getUser)
                .filter(user -> getAuthenticatedUser().getId().equals(user.getId()))
                .isPresent();
    }

    @Override
    public boolean hasPrincipalHavePermissionToRatingResource(Long ratingId) {
        return this.ratingRepository.findById(ratingId)
                .map(Rating::getUser)
                .filter(user -> getAuthenticatedUser().getId().equals(user.getId()))
                .isPresent();
    }

    @Override
    public boolean hasPrincipalHavePermissionToPromotionItemResource(String promotionItemUUID) {
        Optional<PromotionItem> promotionItem = promotionItemRepository.findByPromotionItemUUID(promotionItemUUID);
        if (promotionItem.isEmpty()) {
            return false;
        }

        var registererId = promotionItem.get().getCompany().getRegisterer().getId();
        return registererId.equals(getAuthenticatedUser().personId());
    }

}
