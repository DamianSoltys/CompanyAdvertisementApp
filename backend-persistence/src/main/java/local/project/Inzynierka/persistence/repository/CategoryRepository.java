package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends ApplicationBigRepository<Category> {

    public Category findByName(String name);
}
