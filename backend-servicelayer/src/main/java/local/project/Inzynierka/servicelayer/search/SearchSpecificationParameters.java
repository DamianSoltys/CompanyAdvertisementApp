package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.servicelayer.dto.Voivodeship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SearchSpecificationParameters {

    private String name;
    private Voivodeship voivodeship;
    private String city;
    private String category;
}
