package local.project.Inzynierka.servicelayer.dto;

import local.project.Inzynierka.shared.NullOrNotBlank;
import local.project.Inzynierka.shared.ValidWebProtocolUrl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompanyInfoDto {

    @NullOrNotBlank
    private String companyName;

    @Valid
    private Address address;

    @NullOrNotBlank
    private String description;

    @NullOrNotBlank
    private String category;

    @ValidWebProtocolUrl
    private String companyWebsiteLink;

}
