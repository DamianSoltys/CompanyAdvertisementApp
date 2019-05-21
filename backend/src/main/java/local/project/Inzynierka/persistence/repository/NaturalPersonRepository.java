package local.project.Inzynierka.persistence.repository;


import local.project.Inzynierka.persistence.entity.NaturalPerson;

public interface NaturalPersonRepository extends ApplicationBigRepository<NaturalPerson> {

    NaturalPerson findByPhoneNo(String phoneNo);
}
