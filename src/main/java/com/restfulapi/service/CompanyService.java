package com.restfulapi.service;
import com.restfulapi.entity.Company;
import com.restfulapi.repository.CompanyRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company saveCompany(Company company){
        company.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        company.setCreatedAt(Instant.now());
        return companyRepository.save(company);
    }
    public List<Company> getAllCompany(Pageable pageable){
        return companyRepository.findAll(pageable).getContent();
    }
    public Optional<Company> getCompanyById(long id){
        return companyRepository.findById(id);
    }
    public void deleteCompany(long id){
        companyRepository.deleteById(id);
    }
}
