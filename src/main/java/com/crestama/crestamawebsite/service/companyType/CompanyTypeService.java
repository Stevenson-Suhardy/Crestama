package com.crestama.crestamawebsite.service.companyType;

import com.crestama.crestamawebsite.entity.CompanyType;
import com.crestama.crestamawebsite.repository.CompanyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyTypeService implements ICompanyType {
    private CompanyTypeRepository companyTypeRepository;

    @Autowired
    public CompanyTypeService(CompanyTypeRepository companyTypeRepository) {
        this.companyTypeRepository = companyTypeRepository;
    }

    @Override
    public List<CompanyType> findAll() {
        return companyTypeRepository.findAll();
    }

    @Override
    public CompanyType findById(Long id) {
        Optional<CompanyType> result = companyTypeRepository.findById(id);

        CompanyType companyType = null;

        if (result.isPresent()) {
            companyType = result.get();
        }
        else {
            throw new RuntimeException("Did not find permission id - " + id);
        }

        return companyType;
    }
}
