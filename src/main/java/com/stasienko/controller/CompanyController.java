package com.stasienko.controller;

import com.stasienko.model.Company;
import com.stasienko.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }
    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable("id") UUID id) {
        return companyService.getCompanyById(id);
    }
    @PostMapping("")
    public Company addCompany(@RequestBody Company company) {
        return companyService.addCompany(company);
    }
    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable("id") UUID id, @RequestBody Company updatedCompany) {
        return companyService.updateCompany(id, updatedCompany);
    }
    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable("id") UUID id) {
        companyService.deleteCompany(id);
    }
}