package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
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
public class BranchSearchSpecification extends SearchSpecification<Branch> {

    @Override
    public Predicate toPredicate(Root<Branch> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        nameSpecification(root, criteriaBuilder);
        citySpecification(root, criteriaBuilder);
        voivodeshipSpecification(root, criteriaBuilder);
        categorySpecification(root, criteriaBuilder);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

    }

    @Override
    protected void categorySpecification(Root<Branch> root, CriteriaBuilder criteriaBuilder) {
        if (!CollectionUtils.isEmpty(categories)) {
            Join<Branch, Company> companyJoin = root.join("company");
            Join<Branch, Category> categoryJoin = companyJoin.join("category");
            predicates.add(categoryJoin.<String>get("name").in(categories));
        }
    }

    @Override
    protected void voivodeshipSpecification(Root<Branch> root, CriteriaBuilder criteriaBuilder) {
        if (!CollectionUtils.isEmpty(voivodeships)) {
            Join<Branch, Address> addressJoin = root.join("address");
            Join<Branch, Voivoideship> voivodeshipJoin = addressJoin.join("voivodeship_id");
            predicates.add(voivodeshipJoin.<String>get("name").in(voivodeships));
        }
    }

    @Override
    protected void citySpecification(Root<Branch> root, CriteriaBuilder criteriaBuilder) {
        if (!CollectionUtils.isEmpty(cities)) {
            Join<Branch, Address> addressJoin = root.join("address");
            predicates.add(addressJoin.<String>get("city").in(cities));
        }
    }

    @Override
    protected void nameSpecification(Root<Branch> root, CriteriaBuilder criteriaBuilder) {
        if (!CollectionUtils.isEmpty(names)) {
            predicates.add(root.get("name").in(names));
        }
    }
}
