package local.project.Inzynierka.persistence.projections;

import org.springframework.beans.factory.annotation.Value;

public interface CompanySendMailInfo {

    @Value("#{target.id}")
    Long getCompanyId();

    @Value("#{target.name}")
    String getName();
}
