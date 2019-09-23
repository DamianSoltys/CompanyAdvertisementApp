package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.dto.AddCompanyDto;
import local.project.Inzynierka.servicelayer.dto.NewsletterItemDto;
import local.project.Inzynierka.servicelayer.dto.UpdateCompanyInfoDto;
import local.project.Inzynierka.servicelayer.newsletter.event.OnCreatingNewsletterMailEvent;
import local.project.Inzynierka.servicelayer.services.CompanyManagementPermissionService;
import local.project.Inzynierka.servicelayer.services.CompanyManagementService;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
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

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@RestController
@RequestMapping(value = "/api")
public class CompanyManagementResource {

    private static final String LACK_OF_ADDING_PERMISSION_MESSAGE = "Użytkownik nie ma pozwolenia na dodawanie firm.";
    private static final String COMPANY_ADDING_FAILURE_MESSAGE = "Wystąpił błąd. Nie dodano firmy.";
    private static final String LACK_OF_MANAGING_PERMISSION_MESSAGE = "Użytkownik nie ma pozwolenia na zarządzenie firmą";
    private static final String ORIGIN_HEADER = "Origin";
    private static final String OK_STATUS = "OK";

    private final CompanyManagementService companyManagementService;
    private final CompanyManagementPermissionService companyManagementPermissionService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthFacade authFacade;

    @Autowired
    public CompanyManagementResource(CompanyManagementService companyManagementService,
                                     CompanyManagementPermissionService companyManagementPermissionService,
                                     ApplicationEventPublisher applicationEventPublisher, AuthFacade authFacade) {

        this.companyManagementService = companyManagementService;
        this.companyManagementPermissionService = companyManagementPermissionService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.authFacade = authFacade;
    }

    @RequestMapping(value = "/companies", method = RequestMethod.POST)
    public ResponseEntity<String> addCompany(@Valid @RequestBody final AddCompanyDto addCompanyDto){


        if (!this.companyManagementPermissionService.hasRegisteringAuthority(this.authFacade.getAuthenticatedUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(SimpleJsonFromStringCreator.toJson(LACK_OF_ADDING_PERMISSION_MESSAGE));
        }

        try {
            this.companyManagementService.registerCompany(addCompanyDto, this.authFacade.getAuthenticatedUser());
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

        if (!this.companyManagementPermissionService.hasManagingAuthority(id, this.authFacade.getAuthenticatedUser())) {
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

        if (!this.companyManagementPermissionService.hasManagingAuthority(id, this.authFacade.getAuthenticatedUser())) {
            return new ResponseEntity<>(SimpleJsonFromStringCreator.toJson(LACK_OF_MANAGING_PERMISSION_MESSAGE), HttpStatus.FORBIDDEN);
        }

        return this.companyManagementService.getCompanyInfo(id)
                .map(ResponseEntity::ok).orElse(null);

    }

    @RequestMapping(method = PATCH, value = "/companies/{id}")
    public ResponseEntity<?> updateCompanyInfo(final @PathVariable(value = "id") Long id,
                                               @Valid final @RequestBody UpdateCompanyInfoDto updateCompanyInfoDto) {

        if (!this.companyManagementPermissionService.hasManagingAuthority(id, this.authFacade.getAuthenticatedUser())) {
            return new ResponseEntity<>(SimpleJsonFromStringCreator.toJson(LACK_OF_MANAGING_PERMISSION_MESSAGE), HttpStatus.FORBIDDEN);
        }

        return this.companyManagementService.updateCompanyInfo(id, updateCompanyInfoDto)
                .map(ResponseEntity::ok).orElse(null);

    }

    @RequestMapping(method = DELETE, value = "/companies/{id}")
    public ResponseEntity<?> deleteCompany(final @PathVariable(value = "id") Long id) {

        if (!this.companyManagementPermissionService.hasManagingAuthority(id, this.authFacade.getAuthenticatedUser())) {
            return new ResponseEntity<>(SimpleJsonFromStringCreator.toJson(LACK_OF_MANAGING_PERMISSION_MESSAGE), HttpStatus.FORBIDDEN);
        }

        return this.companyManagementService.deleteCompany(id)
                .map(ResponseEntity::ok).orElse(null);
    }
}
