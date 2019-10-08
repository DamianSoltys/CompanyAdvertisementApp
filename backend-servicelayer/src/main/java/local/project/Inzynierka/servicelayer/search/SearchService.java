package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.SearchableEntity;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private final EntityManager entityManager;

    public SearchService(EntityManager entityManager) {this.entityManager = entityManager;}

    @Transactional
    public Page<Object> searchForEntities(String term, Pageable pageable) {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

// create native Lucene query unsing the query DSL
// alternatively you can write the Lucene query using the Lucene query parser
// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(SearchableEntity.class).get();
        org.apache.lucene.search.Query luceneQuery = qb
                .keyword()
                .onFields("name")
                .matching(term)
                .createQuery();

// wrap Lucene query in a javax.persistence.Query
        Query jpaQuery =
                fullTextEntityManager.createFullTextQuery(luceneQuery, SearchableEntity.class);

        List result = jpaQuery.getResultList();

        List<Object> res = new ArrayList<>();
        result.stream().forEach(entity -> {
            if (entity instanceof Branch) {
                Branch branch = (Branch) entity;
                res.add(SearchableBranchDto.builder()
                                .id(branch.getId())
                                .name(branch.getName())
                                .build());
            }
            if (entity instanceof Company) {
                Company company = (Company) entity;
                res.add(SearchableCompanyDto.builder()
                                .id(company.getId())
                                .name(company.getName())
                                .build());

            }
        });

        return new PageImpl<>(res, pageable, res.size());
    }
}
