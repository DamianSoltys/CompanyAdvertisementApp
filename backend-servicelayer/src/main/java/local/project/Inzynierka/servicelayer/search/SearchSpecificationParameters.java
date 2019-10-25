package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.servicelayer.dto.Voivodeship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SearchSpecificationParameters {

    private List<String> names;
    private List<Voivodeship> voivodeships;
    private List<String> cities;
    private List<String> categories;
}
