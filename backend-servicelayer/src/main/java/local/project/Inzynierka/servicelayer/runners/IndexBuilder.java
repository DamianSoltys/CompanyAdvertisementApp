package local.project.Inzynierka.servicelayer.runners;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Profile(value = "init")
public class IndexBuilder implements CommandLineRunner {

    private final EntityManager entityManager;

    public IndexBuilder(EntityManager entityManager) {this.entityManager = entityManager;}

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
    }
}
