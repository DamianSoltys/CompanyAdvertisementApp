package local.project.Inzynierka.servicelayer.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyLogoUUID {
    private String companyUUID;
    private String logoUUID;
}
