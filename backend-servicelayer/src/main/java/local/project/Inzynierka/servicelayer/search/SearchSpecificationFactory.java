package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.shared.utils.EntityName;

public class SearchSpecificationFactory {

    public static SearchSpecification constructSearchSpecification(SearchSpecificationParameters searchSpecificationParameters,
                                                                   EntityName entityName) {

        SearchSpecification specification;
        if (EntityName.BRANCH.equals(entityName)) {
            specification = BranchSearchSpecification.builder()
                    .category(searchSpecificationParameters.getCategory())
                    .city(searchSpecificationParameters.getCity())
                    .name(searchSpecificationParameters.getName())
                    .voivodeship(searchSpecificationParameters.getVoivodeship())
                    .build();
        } else {
            specification = CompanySearchSpecification.builder()
                    .category(searchSpecificationParameters.getCategory())
                    .city(searchSpecificationParameters.getCity())
                    .name(searchSpecificationParameters.getName())
                    .voivodeship(searchSpecificationParameters.getVoivodeship())
                    .build();
        }

        return specification;
    }
}
