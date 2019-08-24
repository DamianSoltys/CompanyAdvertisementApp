package local.project.Inzynierka.web.controller;

import local.project.Inzynierka.servicelayer.dto.AuthenticatedUserPersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.BecomeNaturalPersonDto;
import local.project.Inzynierka.servicelayer.dto.UpdatePersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.UpdateUserDto;
import local.project.Inzynierka.servicelayer.dto.UserInfoDto;
import local.project.Inzynierka.servicelayer.services.UserFacade;
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
public class AccountController {

    private final UserFacade userFacade;

    public AccountController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/{id}/naturalperson")
    public ResponseEntity<AuthenticatedUserPersonalDataDto> createNaturalPerson(@RequestBody final BecomeNaturalPersonDto naturalPersonDto,
                                                                                @PathVariable(value = "id") Long userId) {

        Optional<AuthenticatedUserPersonalDataDto> person = userFacade.becomeNaturalPerson(naturalPersonDto, userId);
        if (person.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(person.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}/naturalperson/{naturalPersonId}")
    public ResponseEntity<AuthenticatedUserPersonalDataDto> getNaturalPerson(@PathVariable(value = "id") Long id,
                                                                             @PathVariable(value = "naturalPersonId") Long personId) {

        Optional<AuthenticatedUserPersonalDataDto> person = this.userFacade.getUsersPersonalData(id, personId);

        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}")
    public ResponseEntity<UserInfoDto> getUser(@PathVariable(value = "id") Long id) {

        Optional<UserInfoDto> user = this.userFacade.getUser(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/user/{id}")
    public ResponseEntity<String> updateUser(@RequestBody final UpdateUserDto updateUserDto, @PathVariable(value = "id") Long id) {

        if (this.userFacade.changePassword(updateUserDto, id)) {
            return ResponseEntity.ok(SimpleJsonFromStringCreator.toJson("PASSWORD CHANGED"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/user/{id}/naturalperson/{naturalPersonId}")
    public ResponseEntity<AuthenticatedUserPersonalDataDto> updatePersonalData(@RequestBody final UpdatePersonalDataDto updatePersonalDataDto,
                                                                               @PathVariable(value = "id") final Long userId,
                                                                               @PathVariable(value = "naturalPersonId") final Long personId) {

        Optional<AuthenticatedUserPersonalDataDto> person = this.userFacade.updatePersonalData(updatePersonalDataDto, userId, personId);
        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
