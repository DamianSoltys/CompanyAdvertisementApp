package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.AddressRepository;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchPersistenceService {

    private final VoivodeshipRepository voivodeshipRepository;

    private final AddressRepository addressRepository;

    private final BranchRepository branchRepository;

    public BranchPersistenceService(VoivodeshipRepository voivodeshipRepository, AddressRepository addressRepository, BranchRepository branchRepository) {
        this.voivodeshipRepository = voivodeshipRepository;
        this.addressRepository = addressRepository;
        this.branchRepository = branchRepository;
    }

    public void buildAllCompanyBranches(List<Branch> branches, Company createdCompany) {
        branches.forEach(branch -> {
            branch.setCompany(createdCompany);
            branch.setRegisterer(createdCompany.getRegisterer());
            Voivoideship branchVoivodeship = this.voivodeshipRepository.findByName(branch.getAddress().getVoivodeship_id().getName());
            branch.getAddress().setVoivodeship_id(branchVoivodeship);
            Address branchAddress = this.addressRepository.save(branch.getAddress());
            branch.setAddress(branchAddress);
        });
    }

    public Iterable<Branch> saveAll(List<Branch> branches) {
        return this.branchRepository.saveAll(branches);
    }
}
