package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.RestResponse;
import vn.hoidanit.jobhunter.domain.DTO.CompanyDTO;
import vn.hoidanit.jobhunter.service.CompanyService;

@RestController
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/company")
    public ResponseEntity<RestResponse<Company>> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        RestResponse<Company> response = new RestResponse<>();
        response.setData(this.companyService.handleCreateCompany(companyDTO));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}
