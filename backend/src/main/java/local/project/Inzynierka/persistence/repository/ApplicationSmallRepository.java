package local.project.Inzynierka.persistence.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface ApplicationSmallRepository<T> extends PagingAndSortingRepository<T, Short> {
}
