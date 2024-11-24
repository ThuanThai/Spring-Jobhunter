package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class CompanyController {

    @Autowired
    private final CompanyService companyService;

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

    @GetMapping("/company")
    public ResponseEntity<RestResponse<List<Company>>> getAllCompanies() {
        List<Company> companies = this.companyService.handleGetAllCompanies();

        RestResponse<List<Company>> response = new RestResponse<>();
        response.setData(companies);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("company/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable("id") long id) {
        boolean flag = this.companyService.handleDeleteCompany(id);
        if (flag) {
            return ResponseEntity.ok().body("Success Delete Company with id:" + id);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can not find company with id: " + id);
    }
}
