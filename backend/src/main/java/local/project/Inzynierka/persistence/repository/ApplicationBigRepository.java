package local.project.Inzynierka.persistence.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface ApplicationBigRepository<T> extends PagingAndSortingRepository<T, Long>{
}
