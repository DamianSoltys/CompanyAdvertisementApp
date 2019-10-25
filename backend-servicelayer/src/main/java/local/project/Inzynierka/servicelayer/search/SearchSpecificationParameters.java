package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.servicelayer.dto.Voivodeship;
import local.project.Inzynierka.servicelayer.errors.TooManySearchParameterValuesException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SearchSpecificationParameters {

    private static final int UPPER_PARAMETER_NUMBER_LIMIT = 5;

    private List<String> names;
    private List<Voivodeship> voivodeships;
    private List<String> cities;
    private List<String> categories;

    public void validateSpecificationParameters() {
        if (!CollectionUtils.isEmpty(this.names)) {
            if (names.size() > UPPER_PARAMETER_NUMBER_LIMIT) {
                throw new TooManySearchParameterValuesException(UPPER_PARAMETER_NUMBER_LIMIT);
            }
        }
        if (!CollectionUtils.isEmpty(this.voivodeships)) {
            if (voivodeships.size() > UPPER_PARAMETER_NUMBER_LIMIT) {
                throw new TooManySearchParameterValuesException(UPPER_PARAMETER_NUMBER_LIMIT);
            }
        }
        if (!CollectionUtils.isEmpty(this.cities)) {
            if (cities.size() > UPPER_PARAMETER_NUMBER_LIMIT) {
                throw new TooManySearchParameterValuesException(UPPER_PARAMETER_NUMBER_LIMIT);
            }
        }
        if (!CollectionUtils.isEmpty(this.categories)) {
            if (categories.size() > UPPER_PARAMETER_NUMBER_LIMIT) {
                throw new TooManySearchParameterValuesException(UPPER_PARAMETER_NUMBER_LIMIT);
            }
        }
    }
}
