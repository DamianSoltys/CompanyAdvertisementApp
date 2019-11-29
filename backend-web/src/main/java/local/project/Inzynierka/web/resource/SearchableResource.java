package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.servicelayer.dto.address.Voivodeship;
import local.project.Inzynierka.servicelayer.search.SearchService;
import local.project.Inzynierka.servicelayer.search.SearchSpecification;
import local.project.Inzynierka.servicelayer.search.SearchSpecificationFactory;
import local.project.Inzynierka.servicelayer.search.SearchSpecificationParameters;
import local.project.Inzynierka.shared.utils.EntityName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class SearchableResource {

    private final SearchService searchService;

    public SearchableResource(SearchService searchService) {this.searchService = searchService;}

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity<?> search(@RequestParam(value = "q") final String term, Pageable pageable) {

        Page<Object> result = searchService.searchForEntities(term, pageable);

        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search-adv")
    public ResponseEntity<?> searchWithParameters(@RequestParam(value = "type", required = false) final String type,
                                                  @RequestParam(value = "voivodeship", required = false) final List<String> voivodeships,
                                                  @RequestParam(value = "city", required = false) final List<String> cities,
                                                  @RequestParam(value = "name", required = false) final List<String> names,
                                                  @RequestParam(value = "category", required = false) final List<String> categories,
                                                  Pageable pageable) {

        EntityName entityName = getEntityName(type);
        List<Voivodeship> voivodeships1 = getVoivodeships(voivodeships);

        SearchSpecificationParameters searchSpecificationParameters = SearchSpecificationParameters.builder()
                .categories(categories)
                .cities(cities)
                .names(names)
                .voivodeships(voivodeships1)
                .build();
        searchSpecificationParameters.validateSpecificationParameters();

        List<SearchSpecification> specifications = SearchSpecificationFactory.constructSearchSpecifications(searchSpecificationParameters, entityName);

        var result = searchService.searchForEntities(specifications, pageable);

        return ResponseEntity.ok(result);
    }

    private List<Voivodeship> getVoivodeships(List<String> voivodeshipsValues) {
        List<Voivodeship> voivodeships = Collections.emptyList();

        if (voivodeshipsValues != null) {
            voivodeships = voivodeshipsValues.stream()
                    .map(Voivodeship::fromVoivodeship)
                    .collect(Collectors.toList());
        }
        return voivodeships;
    }

    private EntityName getEntityName(String type) {
        EntityName entityName = null;
        if (type != null) {
            entityName = EntityName.fromEntityName(type);
        }
        return entityName;
    }
}
