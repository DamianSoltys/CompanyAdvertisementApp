package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.company.CompanyManagementService;
import local.project.Inzynierka.servicelayer.dto.NewSubscriptionDto;
import local.project.Inzynierka.servicelayer.dto.SubscriptionToCreateDto;
import local.project.Inzynierka.servicelayer.services.NewsletterService;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/newsletter")
@Slf4j
public class NewsletterResource {

    private static final String ANONYMOUS_USER = "anonymousUser";
    private static final String COMPANY_DOES_NOT_EXIST_MESSAGE = "The company doesn't exist.";
    private static final String ORIGIN_HEADER = "Origin";
    private static final String CHECK_EMAIL_REQUEST_MESSAGE = "Ok. Check your email";
    private static final String EMAIL_CONFIRMATION_SUCCESS = "Twój e-mail został potwiedzony";
    private static final String NEWSLETTER_SIGNOUT_SUCCESS = "Zostałe/aś wypisany z listy newslettera.";
    private static final String INVALID_TOKEN = "Nieprawidłowy token";

    private final AuthFacade authFacade;

    private final NewsletterService newsletterService;

    private final CompanyManagementService companyManagementService;

    public NewsletterResource(AuthFacade authFacade, NewsletterService newsletterService, CompanyManagementService companyManagementService) {
        this.authFacade = authFacade;
        this.newsletterService = newsletterService;
        this.companyManagementService = companyManagementService;
    }

    @RequestMapping(method = RequestMethod.POST, name = "/")
    public ResponseEntity<String> subscribeToNewsletter(@Valid @RequestBody final NewSubscriptionDto newSubscriptionDto,
                                                        final HttpServletRequest request) {

        if (!this.companyManagementService.companyExists(newSubscriptionDto.getId())) {
            return ResponseEntity.badRequest().body(SimpleJsonFromStringCreator.toJson(COMPANY_DOES_NOT_EXIST_MESSAGE));
        }
        this.newsletterService.signUpForNewsletter(this.buildSubscriptionToCreateDto(newSubscriptionDto),
                                                   request.getHeader(ORIGIN_HEADER));

        return ResponseEntity.ok().body(SimpleJsonFromStringCreator.toJson(CHECK_EMAIL_REQUEST_MESSAGE));
    }

    private SubscriptionToCreateDto buildSubscriptionToCreateDto(NewSubscriptionDto newSubscriptionDto) {
        String emailToSignUp = authFacade.getAuthentication().getName();
        boolean verified = true;
        if (emailToSignUp.equals(ANONYMOUS_USER)) {
            emailToSignUp = newSubscriptionDto.getEmail();
            verified = false;
        }
        return new SubscriptionToCreateDto(emailToSignUp, newSubscriptionDto.getId(), verified);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/signup")
    public ResponseEntity<String> confirmSigningUp(@RequestParam(name = "token") String token) {
        if (newsletterService.confirmEmail(token)) {
            return ResponseEntity.ok().body(SimpleJsonFromStringCreator.toJson(EMAIL_CONFIRMATION_SUCCESS));
        }
        return ResponseEntity.ok().body(SimpleJsonFromStringCreator.toJson(INVALID_TOKEN));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/signout")
    public ResponseEntity<String> confirmSigningOut(@RequestParam(name = "token") String token) {
        if (newsletterService.confirmSigningOut(token)) {
            return ResponseEntity.ok().body(SimpleJsonFromStringCreator.toJson(NEWSLETTER_SIGNOUT_SUCCESS));
        }
        return ResponseEntity.ok().body(SimpleJsonFromStringCreator.toJson(INVALID_TOKEN));
    }
}
