package local.project.Inzynierka.web.controller;

import local.project.Inzynierka.orchestration.services.UserService;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import local.project.Inzynierka.web.dto.BecomeNaturalPersonDto;
import local.project.Inzynierka.web.mapper.NaturalPersonDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

        NaturalPerson naturalPerson = naturalPersonDtoMapper.map(naturalPersonDto);

        if( userService.becomeNaturalPerson(naturalPerson)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(SimpleJsonFromStringCreator.toJson("OK"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SimpleJsonFromStringCreator.toJson("COŚ NIE TAK"));
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") Long id) {

        User user = this.userService.getUserData(id);

        return ResponseEntity.ok(user);
    }
}
