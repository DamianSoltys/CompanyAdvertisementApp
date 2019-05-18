package local.project.Inzynierka.persistence.repository;


import local.project.Inzynierka.persistence.entity.NaturalPersonEntity;

public interface NaturalPersonRepository extends ApplicationBigRepository<NaturalPersonEntity> {

    NaturalPersonEntity findByPhoneNo(String phoneNo);
}
