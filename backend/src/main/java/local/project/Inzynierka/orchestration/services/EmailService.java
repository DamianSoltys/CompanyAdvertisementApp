package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.domain.model.EmailAddress;

public interface EmailService {

    EmailAddress findByEmail(EmailAddress email);

    EmailAddress saveEmailAddress(EmailAddress emailAddress);
}
