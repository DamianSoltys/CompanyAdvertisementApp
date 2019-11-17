package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends ApplicationBigRepository<Category> {

    Category findByName(String name);

    List<Category> findAll();
}
