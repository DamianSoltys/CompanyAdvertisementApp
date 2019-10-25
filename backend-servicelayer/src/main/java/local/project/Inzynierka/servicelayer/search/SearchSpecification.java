package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.persistence.entity.SearchableEntity;
import local.project.Inzynierka.servicelayer.dto.Voivodeship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class SearchSpecification<T extends SearchableEntity> implements Specification<T> {

    @Builder.Default
    List<Predicate> predicates = new ArrayList<>();

    protected String name;
    protected Voivodeship voivodeship;
    protected String category;
    protected String city;

    protected void nameSpecification(Root<T> root, CriteriaBuilder criteriaBuilder) {
    }

    protected void voivodeshipSpecification(Root<T> root, CriteriaBuilder criteriaBuilder) {
    }

    protected void categorySpecification(Root<T> root, CriteriaBuilder criteriaBuilder) {
    }

    protected void citySpecification(Root<T> root, CriteriaBuilder criteriaBuilder) {
    }
}
