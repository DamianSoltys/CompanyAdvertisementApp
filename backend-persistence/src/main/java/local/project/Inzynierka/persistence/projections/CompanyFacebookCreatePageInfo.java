package local.project.Inzynierka.persistence.projections;

import org.springframework.beans.factory.annotation.Value;

public interface CompanyFacebookCreatePageInfo {

    @Value("#{target.id}")
    Long getCompanyId();

    @Value("#{target.description}")
    String getDescription();

    @Value("#{target.name}")
    String getName();
}
