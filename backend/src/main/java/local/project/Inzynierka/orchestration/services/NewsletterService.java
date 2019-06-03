package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.EmailAddress;

public interface NewsletterService {

    void signUpForNewsletter(EmailAddress emailAddress, Company company);
}
