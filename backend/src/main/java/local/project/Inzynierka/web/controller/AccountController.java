package local.project.Inzynierka.web.controller;

import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.servicelayer.dto.AuthenticatedUserInfoDto;
import local.project.Inzynierka.servicelayer.dto.AuthenticatedUserPersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.BecomeNaturalPersonDto;
import local.project.Inzynierka.servicelayer.dto.UpdatePersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.UpdateUserDto;
import local.project.Inzynierka.servicelayer.services.UserService;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import local.project.Inzynierka.web.mapper.NaturalPersonDtoMapper;
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

    private final NaturalPersonDtoMapper naturalPersonDtoMapper;

    public AccountController(UserService userService, NaturalPersonDtoMapper naturalPersonDtoMapper) {
        this.userService = userService;
        this.naturalPersonDtoMapper = naturalPersonDtoMapper;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/naturalperson")
    public ResponseEntity<String> createNaturalPerson(@RequestBody final BecomeNaturalPersonDto naturalPersonDto) {

        NaturalPerson naturalPerson = this.naturalPersonDtoMapper.map(naturalPersonDto);

        if (userService.becomeNaturalPerson(naturalPerson)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(SimpleJsonFromStringCreator.toJson("OK"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SimpleJsonFromStringCreator.toJson("COŚ NIE TAK"));
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}/naturalperson/{naturalPersonId}")
    public ResponseEntity<AuthenticatedUserPersonalDataDto> getNaturalPerson(@PathVariable(value = "id") Long id,
                                                                             @PathVariable(value = "naturalPersonId") Long personId) {

        Optional<NaturalPerson> person = this.userService.getUsersPersonalData(id, personId);

        if (person.isPresent()) {
            return ResponseEntity.ok(this.naturalPersonDtoMapper.map(person.get()));
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
