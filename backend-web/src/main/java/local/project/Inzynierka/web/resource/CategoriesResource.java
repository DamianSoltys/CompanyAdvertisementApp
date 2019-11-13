package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.servicelayer.company.CategoriesView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/categories")
public class CategoriesResource {

    private final CategoriesView categoriesView;

    public CategoriesResource(CategoriesView categoriesView) {
        this.categoriesView = categoriesView;
    }

    @GetMapping(value = "")
    public ResponseEntity<?> getALlCategories() {
        return ResponseEntity.ok(categoriesView.getCategories());
    }
}
