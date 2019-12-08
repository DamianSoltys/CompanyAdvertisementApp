package local.project.Inzynierka.servicelayer.company;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.FavouriteBranch;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.FavouriteBranchRepository;
import local.project.Inzynierka.servicelayer.dto.branch.FavouriteBranchGetDto;
import local.project.Inzynierka.servicelayer.dto.branch.FavouriteBranchPostDto;
import local.project.Inzynierka.servicelayer.errors.UnexistingBranchException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FavouriteBranchesService {

    private final FavouriteBranchRepository favouriteBranchRepository;
    private final BranchRepository branchRepository;

    public FavouriteBranchesService(FavouriteBranchRepository favouriteBranchRepository, BranchRepository branchRepository) {
        this.favouriteBranchRepository = favouriteBranchRepository;
        this.branchRepository = branchRepository;
    }

    public FavouriteBranchGetDto addFavoriteBranch(FavouriteBranchPostDto favouriteBranchPostDto) {

        Branch branch = branchRepository.findById(favouriteBranchPostDto.getBranchId())
                .orElseThrow(UnexistingBranchException::new);

        Optional<FavouriteBranch> optionalFavouriteBranch =
                favouriteBranchRepository.findByBranch_IdAndUser_Id(favouriteBranchPostDto.getBranchId(),
                                                                    favouriteBranchPostDto.getUserId());

        FavouriteBranch favouriteBranch = optionalFavouriteBranch.orElse(FavouriteBranch.builder()
                                                                                 .uuid(UUID.randomUUID().toString())
                                                                                 .user(User.builder().id(favouriteBranchPostDto.getUserId()).build())
                                                                                 .branch(branch)
                                                                                 .build());

        return Optional.of(favouriteBranchRepository.save(favouriteBranch))
                .map(this::mapFavouriteBranchToDto)
                .orElseThrow(IllegalStateException::new);
    }


    FavouriteBranchGetDto mapFavouriteBranchToDto(FavouriteBranch favouriteBranch) {
        return FavouriteBranchGetDto.builder()
                .userId(favouriteBranch.getUser().getId())
                .branchId(favouriteBranch.getBranch().getId())
                .favoriteBranchUUID(favouriteBranch.getUuid())
                .build();
    }

    public Optional<FavouriteBranchGetDto> getFavouriteBranch(String uuid) {
        return favouriteBranchRepository.findByUuid(uuid).map(this::mapFavouriteBranchToDto);
    }

    public List<FavouriteBranchGetDto> getFavouritesByUser(Long userId) {
        return favouriteBranchRepository.findByUser_Id(userId).stream()
                .map(this::mapFavouriteBranchToDto)
                .collect(Collectors.toList());
    }

    public boolean deleteFavouriteBranch(String favouriteBranchUuid, Long userId) {
        Optional<FavouriteBranch> favouriteBranchOptional = favouriteBranchRepository.findByUuid(favouriteBranchUuid);
        boolean wasDeleted = favouriteBranchOptional
                .map(favouriteBranch -> favouriteBranch.getUser().getId().equals(userId))
                .orElse(false);
        if (wasDeleted) {
            favouriteBranchRepository.delete(favouriteBranchOptional.get());
        }

        return wasDeleted;

    }
}
