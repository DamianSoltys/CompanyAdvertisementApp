package local.project.Inzynierka.web.controller;


import local.project.Inzynierka.orchestration.services.CompanyManagementService;
import local.project.Inzynierka.orchestration.services.NewsletterService;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.shared.AuthenticationFacade;
import local.project.Inzynierka.web.dto.NewSubscriptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/newsletter")
@Slf4j
public class NewsletterController {

    private final AuthenticationFacade authenticationFacade;

    private final NewsletterService newsletterService;

    private final CompanyManagementService companyManagementService;

    public NewsletterController(AuthenticationFacade authenticationFacade, NewsletterService newsletterService, CompanyManagementService companyManagementService) {
        this.authenticationFacade = authenticationFacade;
        this.newsletterService = newsletterService;
        this.companyManagementService = companyManagementService;
    }


    @RequestMapping(method = RequestMethod.POST, name = "/" )
    public void subscribeToNewsletter(@Valid @RequestBody final NewSubscriptionDto newSubscriptionDto) {
        String emailToSignUp = authenticationFacade.getAuthentication().getName();

        if( emailToSignUp.equals("anonymousUser")) {
            emailToSignUp = newSubscriptionDto.getEmail();
        }
        Company company = this.companyManagementService.getThroughBranch(newSubscriptionDto.getId());
        this.newsletterService.signUpForNewsletter(new EmailAddress(emailToSignUp), company );
    }





}
