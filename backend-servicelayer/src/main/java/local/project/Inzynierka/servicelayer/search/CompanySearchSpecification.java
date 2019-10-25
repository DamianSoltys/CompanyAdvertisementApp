package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@SuperBuilder(toBuilder = true)
public class CompanySearchSpecification extends SearchSpecification<Company> {


    @Override
    public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        nameSpecification(root, criteriaBuilder);
        citySpecification(root, criteriaBuilder);
        voivodeshipSpecification(root, criteriaBuilder);
        categorySpecification(root, criteriaBuilder);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    @Override
    protected void categorySpecification(Root<Company> root, CriteriaBuilder criteriaBuilder) {
        if (!CollectionUtils.isEmpty(categories)) {
            Join<Company, Category> categoryJoin = root.join("category");
            predicates.add(categoryJoin.<String>get("name").in(categories));
        }
    }

    @Override
    protected void citySpecification(Root<Company> root, CriteriaBuilder criteriaBuilder) {
        if (!CollectionUtils.isEmpty(cities)) {
            Join<Company, Address> addressJoin = root.join("address");
            predicates.add(addressJoin.<String>get("city").in(cities));
        }
    }

    @Override
    protected void voivodeshipSpecification(Root<Company> root, CriteriaBuilder criteriaBuilder) {
        if (!CollectionUtils.isEmpty(voivodeships)) {
            Join<Company, Address> addressJoin = root.join("address");
            Join<Company, Voivoideship> voivodeshipJoin = addressJoin.join("voivodeship_id");
            predicates.add(voivodeshipJoin.<String>get("name").in(voivodeships));
        }
    }

    @Override
    protected void nameSpecification(Root<Company> root, CriteriaBuilder criteriaBuilder) {
        if (!CollectionUtils.isEmpty(names)) {
            predicates.add(root.<String>get("name").in(names));
        }
    }
}
