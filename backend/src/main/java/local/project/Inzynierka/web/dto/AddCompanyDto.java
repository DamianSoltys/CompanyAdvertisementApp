package local.project.Inzynierka.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCompanyDto {
    private String name;
    private String NIP;
    private String REGON;

    private Address address;

    private String description;
    private String category;

    List<CompanyBranchDto> branches;
}
