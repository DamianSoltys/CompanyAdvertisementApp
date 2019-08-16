package local.project.Inzynierka.web.controller;

import local.project.Inzynierka.servicelayer.dto.AuthenticatedUserInfoDto;
import local.project.Inzynierka.servicelayer.dto.AuthenticatedUserPersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.BecomeNaturalPersonDto;
import local.project.Inzynierka.servicelayer.dto.UpdatePersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.UpdateUserDto;
import local.project.Inzynierka.servicelayer.services.UserService;
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

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/{id}/naturalperson")
    public ResponseEntity<AuthenticatedUserPersonalDataDto> createNaturalPerson(@RequestBody final BecomeNaturalPersonDto naturalPersonDto,
                                                                                @PathVariable(value = "id") Long userId) {

        Optional<AuthenticatedUserPersonalDataDto> person = userService.becomeNaturalPerson(naturalPersonDto, userId);
        if (person.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(person.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}/naturalperson/{naturalPersonId}")
    public ResponseEntity<AuthenticatedUserPersonalDataDto> getNaturalPerson(@PathVariable(value = "id") Long id,
                                                                             @PathVariable(value = "naturalPersonId") Long personId) {

        Optional<AuthenticatedUserPersonalDataDto> person = this.userService.getUsersPersonalData(id, personId);

        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}")
    public ResponseEntity<AuthenticatedUserInfoDto> getUser(@PathVariable(value = "id") Long id) {

        Optional<AuthenticatedUserInfoDto> user = this.userService.getUser(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/user/{id}")
    public ResponseEntity<String> updateUser(@RequestBody final UpdateUserDto updateUserDto, @PathVariable(value = "id") Long id) {

        if (this.userService.changePassword(updateUserDto, id)) {
            return ResponseEntity.ok(SimpleJsonFromStringCreator.toJson("PASSWORD CHANGED"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/user/{id}/naturalperson/{naturalPersonId}")
    public ResponseEntity<AuthenticatedUserPersonalDataDto> updatePersonalData(@RequestBody final UpdatePersonalDataDto updatePersonalDataDto,
                                                                               @PathVariable(value = "id") final Long userId,
                                                                               @PathVariable(value = "naturalPersonId") final Long personId) {

        Optional<AuthenticatedUserPersonalDataDto> person = this.userService.updatePersonalData(updatePersonalDataDto, userId, personId);
        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
