package local.project.Inzynierka.servicelayer.search;

import local.project.Inzynierka.persistence.entity.Address;
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
        if (super.category != null) {
            Join<Company, Category> categoryJoin = root.join("category");
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(categoryJoin.<String>get("name"), super.category)));
        }
    }

    @Override
    protected void citySpecification(Root<Company> root, CriteriaBuilder criteriaBuilder) {
        if (super.city != null) {
            Join<Company, Address> addressJoin = root.join("address");
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(addressJoin.<String>get("city"), super.city)));
        }
    }

    @Override
    protected void voivodeshipSpecification(Root<Company> root, CriteriaBuilder criteriaBuilder) {
        if (super.voivodeship != null) {
            Join<Company, Address> addressJoin = root.join("address");
            Join<Company, Voivoideship> voivodeshipJoin = addressJoin.join("voivodeship_id");
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(voivodeshipJoin.<String>get("name"), super.voivodeship.toString())));
        }
    }

    @Override
    protected void nameSpecification(Root<Company> root, CriteriaBuilder criteriaBuilder) {
        if (super.name != null) {
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("name"), super.name)));
        }
    }
}
