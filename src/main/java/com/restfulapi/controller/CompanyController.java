package com.restfulapi.controller;

import com.restfulapi.entity.Company;
import com.restfulapi.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("companies")
    public ResponseEntity<Company>newCompany(@RequestBody @Valid Company company){
        Company newCompany=companyService.saveCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }
}
