package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.servicelayer.dto.Voivodeship;
import local.project.Inzynierka.servicelayer.search.SearchService;
import local.project.Inzynierka.servicelayer.search.SearchSpecificationFactory;
import local.project.Inzynierka.servicelayer.search.SearchSpecificationParameters;
import local.project.Inzynierka.shared.utils.EntityName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> searchWithParameters5(@RequestParam(value = "type", required = false) final String type,
                                                   @RequestParam(value = "voivodeship", required = false) final String voivodeship,
                                                   @RequestParam(value = "city", required = false) final String city,
                                                   @RequestParam(value = "name", required = false) final String name,
                                                   @RequestParam(value = "category", required = false) final String category,
                                                   Pageable pageable) {


        EntityName entityName = getEntityName(type);
        Voivodeship voivodeship1 = getVoivodeship(voivodeship);

        SearchSpecificationParameters searchSpecificationParameters = SearchSpecificationParameters.builder()
                .category(category)
                .city(city)
                .name(name)
                .voivodeship(voivodeship1)
                .build();
        Specification<?> specification = SearchSpecificationFactory.constructSearchSpecification(searchSpecificationParameters, entityName);

        Page<Object> result = searchService.searchForEntities(specification, pageable);

        return ResponseEntity.ok(result);
    }

    private Voivodeship getVoivodeship(@RequestParam(value = "voivodeship", required = false) String voivodeship) {
        Voivodeship voivodeship1 = null;
        if (voivodeship != null) {
            voivodeship1 = Voivodeship.fromVoivodeship(voivodeship);
        }
        return voivodeship1;
    }

    private EntityName getEntityName(@RequestParam(value = "type", required = false) String type) {
        EntityName entityName = null;
        if (type != null) {
            entityName = EntityName.fromEntityName(type);
        }
        return entityName;
    }
}
