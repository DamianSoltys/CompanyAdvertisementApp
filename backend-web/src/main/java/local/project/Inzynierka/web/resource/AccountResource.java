package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.dto.AuthenticatedUserPersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.BecomeNaturalPersonDto;
import local.project.Inzynierka.servicelayer.dto.UpdatePersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.UpdateUserDto;
import local.project.Inzynierka.servicelayer.dto.UserInfoDto;
import local.project.Inzynierka.servicelayer.errors.NotAuthorizedAccessToResourceException;
import local.project.Inzynierka.servicelayer.services.UserFacade;
import local.project.Inzynierka.shared.UserAccount;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class AccountResource {

    private final UserFacade userFacade;

    private final AuthFacade authFacade;

    public AccountResource(UserFacade userFacade, AuthFacade authFacade) {
        this.userFacade = userFacade;
        this.authFacade = authFacade;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/{id}/naturalperson")
    public ResponseEntity<AuthenticatedUserPersonalDataDto> createNaturalPerson(@RequestBody final BecomeNaturalPersonDto naturalPersonDto,
                                                                                @PathVariable(value = "id") Long userId) {

        if (authFacade.hasPrincipalHavePermissionToUserResource(userId)) {
            UserAccount userAccount = this.authFacade.getAuthenticatedUser();
            Optional<AuthenticatedUserPersonalDataDto> person = userFacade.becomeNaturalPerson(naturalPersonDto, userAccount);
            if (person.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(person.get());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }

        throw new NotAuthorizedAccessToResourceException("Authenticated user has got no access to this resource.");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}/naturalperson/{naturalPersonId}")
    public ResponseEntity<AuthenticatedUserPersonalDataDto> getNaturalPerson(@PathVariable(value = "id") Long id,
                                                                             @PathVariable(value = "naturalPersonId") Long personId) {

        UserAccount authenticatedUser = this.authFacade.getAuthenticatedUser();
        Optional<AuthenticatedUserPersonalDataDto> person;

        if (this.authFacade.hasPrincipalHavePermissionToUserResource(id)) {
            if (authenticatedUser.isNaturalPersonRegistered() &&
                    authenticatedUser.personId().equals(personId)) {

                person = this.userFacade.getUsersPersonalData(authenticatedUser);
                return ResponseEntity.ok(person.get());

            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}")
    public ResponseEntity<UserInfoDto> getUser(@PathVariable(value = "id") Long id) {

        Optional<UserInfoDto> user;
        if (authFacade.hasPrincipalHavePermissionToUserResource(id)) {

            user = this.userFacade.getUser(id);
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/user/{id}")
    public ResponseEntity<String> updateUser(@RequestBody final UpdateUserDto updateUserDto, @PathVariable(value = "id") Long userId) {

        if (authFacade.hasPrincipalHavePermissionToUserResource(userId)) {

            UserAccount userAccount = this.authFacade.getAuthenticatedUser();
            this.userFacade.changePassword(updateUserDto, userAccount);
            return ResponseEntity.ok(SimpleJsonFromStringCreator.toJson("PASSWORD CHANGED"));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/user/{id}/naturalperson/{naturalPersonId}")
    public ResponseEntity<AuthenticatedUserPersonalDataDto> updatePersonalData(@RequestBody final UpdatePersonalDataDto updatePersonalDataDto,
                                                                               @PathVariable(value = "id") final Long userId,
                                                                               @PathVariable(value = "naturalPersonId") final Long personId) {

        if (this.authFacade.hasPrincipalHavePermissionToUserResource(userId)) {
            if (this.authFacade.hasPrincipalHavePermissionToNaturalPersonResource(userId, personId)) {
                UserAccount userAccount = this.authFacade.getAuthenticatedUser();
                Optional<AuthenticatedUserPersonalDataDto> person = this.userFacade.updatePersonalData(updatePersonalDataDto, userAccount);
                return ResponseEntity.ok(person.get());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") final Long userId) {

        if (this.authFacade.hasPrincipalHavePermissionToUserResource(userId)) {
            return this.userFacade.deleteAccount(this.authFacade.getAuthenticatedUser())
                    .map(ResponseEntity::ok)
                    .orElse(null);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{id}/naturalperson/{naturalPersonId}")
    public ResponseEntity<?> deletePersonalData(@PathVariable(value = "id") final Long userId,
                                                @PathVariable(value = "naturalPersonId") final Long personId) {

        if (this.authFacade.hasPrincipalHavePermissionToUserResource(userId)) {
            if (this.authFacade.hasPrincipalHavePermissionToNaturalPersonResource(userId, personId)) {
                return this.userFacade.deletePersonalData(this.authFacade.getAuthenticatedUser())
                        .map(ResponseEntity::ok)
                        .orElse(null);
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
