package local.project.Inzynierka.servicelayer.dto.company;

import local.project.Inzynierka.servicelayer.dto.address.Address;
import local.project.Inzynierka.servicelayer.validation.NullOrNotBlank;
import local.project.Inzynierka.servicelayer.validation.ValidWebProtocolUrl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompanyInfoDto {

    @NullOrNotBlank
    private String name;

    @Valid
    private Address address;

    @NullOrNotBlank
    private String description;

    @NullOrNotBlank
    private String category;

    @ValidWebProtocolUrl
    private String companyWebsiteUrl;

    @NullOrNotBlank
    private String nip;

    @NullOrNotBlank
    private String regon;

}
