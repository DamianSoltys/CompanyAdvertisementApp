package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.company.CompanyManagementPermissionService;
import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemAddedEvent;
import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemService;
import local.project.Inzynierka.shared.UserAccount;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/api/pi")
@RestController
public class PromotionItemResource {

    private static final String ORIGIN_HEADER = "Origin";

    private final PromotionItemService promotionItemService;
    private final CompanyManagementPermissionService companyManagementPermissionService;
    private final AuthFacade authFacade;

    public PromotionItemResource(PromotionItemService promotionItemService,
                                 CompanyManagementPermissionService companyManagementPermissionService,
                                 AuthFacade authFacade) {

        this.promotionItemService = promotionItemService;
        this.companyManagementPermissionService = companyManagementPermissionService;
        this.authFacade = authFacade;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<?> addPromotionItem(@RequestBody @Valid PromotionItemAddedEvent promotionItemAddedEvent,
                                              HttpServletRequest httpServletRequest) {

        UserAccount userAccount = authFacade.getAuthenticatedUser();
        if (!companyManagementPermissionService.hasManagingAuthority(promotionItemAddedEvent.getCompanyId(), userAccount)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        promotionItemAddedEvent.setAppUrl(httpServletRequest.getHeader(ORIGIN_HEADER));
        promotionItemService.addPromotionItem(promotionItemAddedEvent);

        return ResponseEntity.ok(SimpleJsonFromStringCreator.toJson("OK"));
    }
}
