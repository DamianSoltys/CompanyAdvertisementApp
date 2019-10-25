package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Category;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import lombok.experimental.SuperBuilder;

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
        if (super.category != null) {
            Join<Branch, Company> companyJoin = root.join("company");
            Join<Branch, Category> categoryJoin = companyJoin.join("category");
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(categoryJoin.<String>get("name"), super.category)));
        }
    }

    @Override
    protected void voivodeshipSpecification(Root<Branch> root, CriteriaBuilder criteriaBuilder) {
        if (super.voivodeship != null) {
            Join<Branch, Address> addressJoin = root.join("address");
            Join<Branch, Voivoideship> voivodeshipJoin = addressJoin.join("voivodeship_id");
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(voivodeshipJoin.<String>get("name"), super.voivodeship.toString())));
        }
    }

    @Override
    protected void citySpecification(Root<Branch> root, CriteriaBuilder criteriaBuilder) {
        if (super.city != null) {
            Join<Branch, Address> addressJoin = root.join("address");
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(addressJoin.<String>get("city"), super.city)));
        }
    }

    @Override
    protected void nameSpecification(Root<Branch> root, CriteriaBuilder criteriaBuilder) {
        if (super.name != null) {
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("name"), super.name)));
        }
    }
}
