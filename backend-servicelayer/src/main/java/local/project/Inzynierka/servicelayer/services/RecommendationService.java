package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Rating;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.RatingRepository;
import local.project.Inzynierka.servicelayer.dto.recommendation.RecommendationDto;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final RatingRepository ratingRepository;
    private final BranchRepository branchRepository;

    public RecommendationService(RatingRepository ratingRepository, BranchRepository branchRepository) {
        this.ratingRepository = ratingRepository;
        this.branchRepository = branchRepository;
    }

    @Transactional
    public List<RecommendationDto> getRecommendedBranches(List<String> categories, UserAccount userAccount) {

        return categories.stream()
                .map(branchRepository::findByCompany_Category_Name)
                .flatMap(Collection::stream)
                .map(branch -> buildRecommendation(userAccount, branch))
                .collect(Collectors.toList());

    }

    private RecommendationDto buildRecommendation(UserAccount userAccount, Branch branch) {
        return RecommendationDto.builder()
                .averageRating(ratingRepository.getBranchAverageRating(branch.getId()))
                .currentUserRating(getCurrentUserRating(userAccount, branch))
                .branchId(branch.getId())
                .category(branch.getCompany().getCategory().getName())
                .companyId(branch.getCompany().getId())
                .geoX(branch.getGeoX())
                .geoY(branch.getGeoY())
                .getLogoURL(branch.getPhotoPath())
                .name(branch.getName())
                .build();
    }

    private Integer getCurrentUserRating(UserAccount userAccount, Branch branch) {
        return Optional.ofNullable(userAccount)
                                   .map(account -> ratingRepository.findByBranchAndUser_Id(branch, account.getId()))
                                   .map(Rating::getRating)
                                   .orElse(null);
    }
}
