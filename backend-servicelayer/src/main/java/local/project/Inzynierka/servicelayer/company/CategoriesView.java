package local.project.Inzynierka.servicelayer.company;

import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriesView {

    private final CategoryRepository categoryRepository;

    public CategoriesView(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<String> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}
