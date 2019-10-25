package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.shared.utils.EntityName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchSpecificationFactory {

    public static List<SearchSpecification> constructSearchSpecifications(SearchSpecificationParameters searchSpecificationParameters,
                                                                          EntityName entityName) {

        List<SearchSpecification> specification;
        if (EntityName.BRANCH.equals(entityName)) {
            specification = Collections.singletonList(buildBranchSearchSpecification(searchSpecificationParameters));
        } else if (EntityName.COMPANY.equals(entityName)) {
            specification = Collections.singletonList(buildCompanySearchSpecification(searchSpecificationParameters));
        } else {
            specification = Arrays.asList(
                    buildBranchSearchSpecification(searchSpecificationParameters),
                    buildCompanySearchSpecification(searchSpecificationParameters)
            );
        }

        return specification;
    }

    private static CompanySearchSpecification buildCompanySearchSpecification(SearchSpecificationParameters searchSpecificationParameters) {
        return CompanySearchSpecification.builder()
                .categories(searchSpecificationParameters.getCategories())
                .cities(searchSpecificationParameters.getCities())
                .names(searchSpecificationParameters.getNames())
                .voivodeships(searchSpecificationParameters.getVoivodeships())
                .build();
    }

    private static BranchSearchSpecification buildBranchSearchSpecification(SearchSpecificationParameters searchSpecificationParameters) {
        return BranchSearchSpecification.builder()
                .categories(searchSpecificationParameters.getCategories())
                .cities(searchSpecificationParameters.getCities())
                .names(searchSpecificationParameters.getNames())
                .voivodeships(searchSpecificationParameters.getVoivodeships())
                .build();
    }
}
