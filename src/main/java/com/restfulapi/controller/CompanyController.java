package com.restfulapi.controller;

import com.restfulapi.annotation.ApiMessage;
import com.restfulapi.entity.Company;
import com.restfulapi.exception.ResourceNotFoundException;
import com.restfulapi.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("companies")
    @ApiMessage("Create Company")
    public ResponseEntity<Company>newCompany(@RequestBody @Valid Company company){
        Company newCompany=companyService.saveCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }
    @GetMapping("companies")
    @ApiMessage("Fetch All Company")
    public ResponseEntity<List<Company>>getAllCompany(@RequestParam(value = "page",required = false) int page, @RequestParam(value = "size",required = false) int size){
        Pageable pageable= PageRequest.of(page-1,size);
        return ResponseEntity.ok().body(companyService.getAllCompany(pageable));
    }
    @GetMapping("companies/{id}")
    @ApiMessage("Fetch Company")
    public Company getCompanyById(@PathVariable long id){
        return companyService.getCompanyById(id).orElseThrow(() -> new ResourceNotFoundException(("Không tìm thấy Company với ID: " + id)));
    }
    @PutMapping("companies")
    @ApiMessage("Update Company")
    public ResponseEntity<?> updateCompany(@RequestBody Company company){
        Optional<Company> getCompany=companyService.getCompanyById(company.getId());
        if (getCompany.isPresent()){
            Company updateCompany=getCompany.get();
            updateCompany.setName(company.getName());
            updateCompany.setDescription(company.getDescription());
            updateCompany.setAddress(company.getAddress());
            updateCompany.setUpdatedAt(Instant.now());
            updateCompany.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            companyService.saveCompany(updateCompany);
            return ResponseEntity.ok(updateCompany);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy Company với ID: " + company.getId());
    }
    @DeleteMapping("/companies/{id}")
    @ApiMessage("Delete Company")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok("delete Company By id" + id);
    }
}
