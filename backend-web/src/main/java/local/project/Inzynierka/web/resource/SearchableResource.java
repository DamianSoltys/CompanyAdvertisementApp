package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.servicelayer.dto.Voivodeship;
import local.project.Inzynierka.servicelayer.search.SearchService;
import local.project.Inzynierka.servicelayer.search.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @RequestMapping(method = RequestMethod.GET, value = "/searchAdv")
    public ResponseEntity<?> searchWithParameters5(@RequestParam(value = "type", required = false) final String type,
                                                   @RequestParam(value = "voivodeship", required = false) final Voivodeship voivodeship,
                                                   @RequestParam(value = "city", required = false) final String city,
                                                   @RequestParam(value = "name", required = false) final String name,
                                                   @RequestParam(value = "category", required = false) final String category,
                                                   Pageable pageable) {

        SearchSpecification searchSpecification = SearchSpecification.builder()
                .type(type)
                .category(category)
                .city(city)
                .name(name)
                .voivodeship(voivodeship)
                .build();
        Page<Object> result = searchService.searchForEntities(searchSpecification, pageable);

        return ResponseEntity.ok(result);
    }
}
