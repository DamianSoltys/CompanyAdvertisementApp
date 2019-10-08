package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.servicelayer.search.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class SearchableResource {

    private final SearchService searchService;

    public SearchableResource(SearchService searchService) {this.searchService = searchService;}

    @RequestMapping(method = RequestMethod.GET, value = "/search/{term}")
    public ResponseEntity<?> search(@PathVariable(value = "term") final String term, Pageable pageable) {

        Page<Object> result = searchService.searchForEntities(term, pageable);

        return ResponseEntity.ok(result);
    }
}
