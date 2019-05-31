package local.project.Inzynierka.web.controller;

import local.project.Inzynierka.orchestration.services.UserService;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import local.project.Inzynierka.web.dto.BecomeNaturalPersonDto;
import local.project.Inzynierka.web.mapper.NaturalPersonDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class AccountController {


    @Autowired
    private UserService userService;

    @Autowired
    private NaturalPersonDtoMapper naturalPersonDtoMapper;

    @RequestMapping(method = RequestMethod.POST, value = "/user/naturalperson")
    public ResponseEntity<String> createNaturalPerson(@RequestBody final BecomeNaturalPersonDto naturalPersonDto) {

        NaturalPerson naturalPerson = naturalPersonDtoMapper.map(naturalPersonDto);

        if( userService.becomeNaturalPerson(naturalPerson)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(SimpleJsonFromStringCreator.toJson("OK"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SimpleJsonFromStringCreator.toJson("COŚ NIE TAK"));
        }
    }



}
