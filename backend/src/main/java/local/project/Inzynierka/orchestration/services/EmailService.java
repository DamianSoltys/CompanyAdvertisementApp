package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.EmailAddress;

public interface EmailService {

    EmailAddress findByEmail(EmailAddress email);

    EmailAddress saveEmailAddress(EmailAddress emailAddress);
}
