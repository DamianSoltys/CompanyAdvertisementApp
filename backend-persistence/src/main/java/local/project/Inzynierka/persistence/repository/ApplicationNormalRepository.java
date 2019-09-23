package local.project.Inzynierka.persistence.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface ApplicationNormalRepository<T> extends PagingAndSortingRepository<T, Integer> {
}
