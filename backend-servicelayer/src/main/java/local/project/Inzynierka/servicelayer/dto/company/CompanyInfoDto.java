package local.project.Inzynierka.servicelayer.dto.company;

import local.project.Inzynierka.servicelayer.dto.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class CompanyInfoDto {

    private Long companyId;
    private String companyName;
    private String registererFullname;
    private String category;
    private String companyWebsiteUrl;
    private String description;
    private Address address;
    private String REGON;
    private String NIP;
    private List<Long> branchesIDs;
    private String logoURL;
    private String logoKey;
    private Boolean hasLogoAdded;

}
