package local.project.Inzynierka.web.controller;

import local.project.Inzynierka.servicelayer.dto.AddCompanyDto;
import local.project.Inzynierka.servicelayer.dto.NewsletterItemDto;
import local.project.Inzynierka.servicelayer.newsletter.event.OnCreatingNewsletterMailEvent;
import local.project.Inzynierka.servicelayer.services.CompanyManagementService;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import local.project.Inzynierka.web.security.CompanyManagementPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api")
public class CompanyManagementController {

    private static final String LACK_OF_ADDING_PERMISSION_MESSAGE = "Użytkownik nie ma pozwolenia na dodawanie firm.";
    private static final String COMPANY_ADDING_FAILURE_MESSAGE = "Wystąpił błąd. Nie dodano firmy.";
    private static final String LACK_OF_MANAGING_PERMISSION_MESSAGE = "Użytkownik nie ma pozwolenia na zarządzenie firmą";
    private static final String ORIGIN_HEADER = "Origin";
    private static final String OK_STATUS = "OK";

    private final CompanyManagementService companyManagementService;

    private final CompanyManagementPermissionService companyManagementPermissionService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public CompanyManagementController(CompanyManagementService companyManagementService,
                                       CompanyManagementPermissionService companyManagementPermissionService,
                                       ApplicationEventPublisher applicationEventPublisher){

        this.companyManagementService = companyManagementService;
        this.companyManagementPermissionService = companyManagementPermissionService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @RequestMapping(value = "/companies", method = RequestMethod.POST)
    public ResponseEntity<String> addCompany(@Valid @RequestBody final AddCompanyDto addCompanyDto){


        if (!this.companyManagementPermissionService.hasRegisteringAuthority()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(SimpleJsonFromStringCreator.toJson(LACK_OF_ADDING_PERMISSION_MESSAGE));
        }

        try {
            this.companyManagementService.registerCompany(addCompanyDto);
            return ResponseEntity.ok().body(SimpleJsonFromStringCreator.toJson(OK_STATUS));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(SimpleJsonFromStringCreator.toJson(COMPANY_ADDING_FAILURE_MESSAGE));
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/companies/{id}/newsletters")
    public ResponseEntity<String> sendEmailToNewsletterRecipients(final @PathVariable(value = "id") Long id,
                                                                  @Valid @RequestBody NewsletterItemDto newsletterItemDto,
                                                                  HttpServletRequest request){

        if (!this.companyManagementPermissionService.hasManagingAuthority(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(SimpleJsonFromStringCreator.toJson(LACK_OF_MANAGING_PERMISSION_MESSAGE));
        }

        this.applicationEventPublisher.publishEvent(
                new OnCreatingNewsletterMailEvent(
                        newsletterItemDto.getMessage(),
                        newsletterItemDto.getSubject(),
                        request.getHeader(ORIGIN_HEADER),
                        id));

        return ResponseEntity.status(HttpStatus.OK)
                .body(SimpleJsonFromStringCreator
                              .toJson(OK_STATUS));

    }

    @RequestMapping(method = GET, value = "/companies/{id}")
    public ResponseEntity<?> getCompanyInfo(final @PathVariable(value = "id") Long id) {

        if (!this.companyManagementPermissionService.hasManagingAuthority(id)) {
            return new ResponseEntity<>(SimpleJsonFromStringCreator.toJson(LACK_OF_MANAGING_PERMISSION_MESSAGE), HttpStatus.FORBIDDEN);
        }

        return this.companyManagementService.getCompanyInfo(id)
                .map(ResponseEntity::ok).orElse(null);

    }
}
