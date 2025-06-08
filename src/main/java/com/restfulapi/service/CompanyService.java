package com.restfulapi.service;
import com.restfulapi.entity.Company;
import com.restfulapi.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company saveCompany(Company company){
        return companyRepository.save(company);
    }
}
