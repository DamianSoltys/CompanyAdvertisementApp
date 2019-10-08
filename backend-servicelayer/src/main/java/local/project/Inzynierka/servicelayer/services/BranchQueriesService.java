package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.servicelayer.dto.CompanyBranchDto;
import local.project.Inzynierka.servicelayer.dto.PersistedBranchDto;
import local.project.Inzynierka.servicelayer.dto.mapper.BranchMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BranchQueriesService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public BranchQueriesService(BranchRepository branchRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    public Optional<CompanyBranchDto> getById(Long branchId) {
        return branchRepository.findById(branchId)
                .map(branchMapper::mapInputBranch);
    }

    public Page<PersistedBranchDto> getAll(Pageable pageable) {
        return branchRepository.findAll(pageable)
                .map(branchMapper::mapPersistedBranch);
    }
}
