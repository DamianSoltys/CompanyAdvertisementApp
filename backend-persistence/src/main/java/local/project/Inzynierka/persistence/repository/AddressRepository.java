package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Address;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends ApplicationBigRepository<Address> {

    @Modifying
    @Query(value = "delete from addresses where id in ?1", nativeQuery = true)
    void deleteAllById(List<Long> addresses);
}
