package local.project.Inzynierka.web.controller;


import local.project.Inzynierka.orchestration.services.CompanyManagementService;
import local.project.Inzynierka.orchestration.services.NewsletterService;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.NewsletterSubscription;
import local.project.Inzynierka.shared.AuthenticationFacade;
import local.project.Inzynierka.web.dto.NewSubscriptionDto;
import local.project.Inzynierka.web.newsletter.event.OnNewsletterSignUpEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/newsletter")
@Slf4j
public class NewsletterController {

    private final AuthenticationFacade authenticationFacade;

    private final NewsletterService newsletterService;

    private final CompanyManagementService companyManagementService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public NewsletterController(AuthenticationFacade authenticationFacade, NewsletterService newsletterService, CompanyManagementService companyManagementService, ApplicationEventPublisher applicationEventPublisher) {
        this.authenticationFacade = authenticationFacade;
        this.newsletterService = newsletterService;
        this.companyManagementService = companyManagementService;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @RequestMapping(method = RequestMethod.POST, name = "/" )
    public void subscribeToNewsletter(@Valid @RequestBody final NewSubscriptionDto newSubscriptionDto,
                                      final HttpServletRequest request) {
        String emailToSignUp = authenticationFacade.getAuthentication().getName();
        boolean verified = true;
        if( emailToSignUp.equals("anonymousUser")) {
            emailToSignUp = newSubscriptionDto.getEmail();
            verified = false;
        }
        Company company = this.companyManagementService.getThroughBranch(newSubscriptionDto.getId());
        NewsletterSubscription newsletterSubscription =
                this.newsletterService.signUpForNewsletter(new EmailAddress(emailToSignUp), company, verified );
        if(!verified) {
            applicationEventPublisher.publishEvent(
                    new OnNewsletterSignUpEvent(newsletterSubscription, request.getHeader("Origin")));
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/signup")
    public ResponseEntity<String> confirmSigningUp(@RequestParam(name = "token") String token ) {
        if( newsletterService.confirmEmail(token)) {
            return ResponseEntity.ok().body("{\"data\":\"Twój e-mail został potwiedzony\"}");
        }
        return ResponseEntity.ok().body("{\"data\":\"Nieprawidłowy token\"}");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/signout")
    public ResponseEntity<String> confirmSigningOut(@RequestParam(name = "token") String token ) {
        if( newsletterService.confirmSigningOut(token)) {
            return ResponseEntity.ok().body("{\"data\":\"Zostałe/aś wypisany z listy newslettera.\"}");
        }
        return ResponseEntity.ok().body("{\"data\":\"Nieprawidłowy token\"}");
    }



}
