package local.project.Inzynierka.web.controller;

import local.project.Inzynierka.orchestration.services.CompanyManagementService;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.web.dto.AddCompanyDto;
import local.project.Inzynierka.web.mapper.CompanyExtractor;
import local.project.Inzynierka.web.security.CompanyManagementPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CompanyManagementController {

    private final CompanyManagementService companyManagementService;

    private final CompanyManagementPermissionService companyManagementPermissionService;

    @Autowired
    public CompanyManagementController(CompanyManagementService companyManagementService, CompanyManagementPermissionService companyManagementPermissionService){
        this.companyManagementService = companyManagementService;
        this.companyManagementPermissionService = companyManagementPermissionService;
    }

    @RequestMapping(value = "/companies", method = RequestMethod.POST)
    public ResponseEntity<String> addCompany(@Valid @RequestBody final AddCompanyDto addCompanyDto){


        if( !companyManagementPermissionService.hasRegisteringAuthority()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Użytkownik nie ma pozwolenia na dodawanie firm.");
        }

        CompanyExtractor companyExtractor = new CompanyExtractor(addCompanyDto);
        List<Branch> branches = companyExtractor.getBranches();
        Company company = companyExtractor.getCompany();

        try {
            this.companyManagementService.registerCompany(company, branches);
            return ResponseEntity.ok().body("OK");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("COŚ POSZŁO NIE TAK");
        }

    }

}
