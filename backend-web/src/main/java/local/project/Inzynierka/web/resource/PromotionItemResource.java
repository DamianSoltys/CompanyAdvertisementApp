package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.company.CompanyManagementPermissionService;
import local.project.Inzynierka.servicelayer.dto.promotionitem.GetPromotionItemDto;
import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemService;
import local.project.Inzynierka.servicelayer.promotionitem.event.PromotionItemAddedEvent;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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

    @PostMapping(value = "")
    public ResponseEntity<?> addPromotionItem(@RequestBody @Valid PromotionItemAddedEvent promotionItemAddedEvent,
                                              HttpServletRequest httpServletRequest) {
        ResponseEntity<?> result;

        UserAccount userAccount = authFacade.getAuthenticatedUser();
        if (!companyManagementPermissionService.hasManagingAuthority(promotionItemAddedEvent.getCompanyId(), userAccount)) {
            result = ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            promotionItemAddedEvent.setAppUrl(httpServletRequest.getHeader(ORIGIN_HEADER));
            result = ResponseEntity.ok(promotionItemService.addPromotionItem(promotionItemAddedEvent));
        }

        return result;
    }

    @PutMapping(value = "/{promotionItemUUID}/adding")
    public ResponseEntity<?> getAddingConfirmation(@PathVariable(value = "promotionItemUUID") String promotionItemUUID) {

        ResponseEntity<?> result;
        if (!authFacade.hasPrincipalHavePermissionToPromotionItemResource(promotionItemUUID)) {
            result = ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            result = ResponseEntity.ok(promotionItemService.finalizePromotionItemAdding(promotionItemUUID));
        }

        return result;
    }


    @PutMapping( value = "/{promotionItemUUID}/sending")
    public ResponseEntity<?> getSendingConfirmation(@PathVariable(value = "promotionItemUUID") String promotionItemUUID) {

        ResponseEntity<?> result;
        if (!authFacade.hasPrincipalHavePermissionToPromotionItemResource(promotionItemUUID)) {
            result = ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            result = ResponseEntity.ok(promotionItemService.finalizePromotionItemSending(promotionItemUUID));
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<List<GetPromotionItemDto>> getPromotionItems(@RequestParam(value = "companyId") Long companyId) {

        ResponseEntity<List<GetPromotionItemDto>> result;

        UserAccount userAccount = authFacade.getAuthenticatedUser();
        if (!companyManagementPermissionService.hasManagingAuthority(companyId, userAccount)) {
            result = ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            result = ResponseEntity.ok(promotionItemService.getPromotionItems(companyId));
        }

        return result;
    }
}
