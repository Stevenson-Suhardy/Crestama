package com.crestama.crestamawebsite.service.companyType;

import com.crestama.crestamawebsite.entity.CompanyType;

import java.util.List;

public interface ICompanyType {
    List<CompanyType> findAll();

    CompanyType findById(Long id);
}
