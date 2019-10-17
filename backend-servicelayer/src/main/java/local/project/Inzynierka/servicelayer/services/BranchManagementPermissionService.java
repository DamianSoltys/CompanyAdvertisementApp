package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchManagementPermissionService {

    private final UserRepository userRepository;

    private final BranchRepository branchRepository;

    public BranchManagementPermissionService(UserRepository userRepository, BranchRepository branchRepository) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
    }

    public boolean hasManagingAuthority(Long branchId, UserAccount userAccount) {
        User user = userRepository.getByAddressEmail(userAccount.getEmail());

        if (user == null) {
            return false;
        }
        NaturalPerson naturalPerson = user.getNaturalPerson();
        if (naturalPerson == null) {
            return false;
        }

        Branch requestedBranch = branchRepository.findById(branchId).orElse(new Branch());
        List<Branch> userBranches = branchRepository.findByRegisterer(naturalPerson);

        return userBranches.stream().anyMatch(usersBranch -> usersBranch.equals(requestedBranch));
    }
}
