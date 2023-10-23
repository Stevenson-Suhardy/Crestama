package com.crestama.crestamawebsite.service.salesReportForm;

import com.crestama.crestamawebsite.entity.SalesReportForm;
import com.crestama.crestamawebsite.repository.SalesReportFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SalesReportFormService implements ISalesReportForm {
    private SalesReportFormRepository salesReportFormRepository;

    @Autowired
    public SalesReportFormService(SalesReportFormRepository salesReportFormRepository) {
        this.salesReportFormRepository = salesReportFormRepository;
    }

    @Override
    public List<SalesReportForm> findAll() {
        return salesReportFormRepository.findAll();
    }

    @Override
    public SalesReportForm findById(Long id) {
        Optional<SalesReportForm> result = salesReportFormRepository.findById(id);

        SalesReportForm salesReportForm = null;

        if (result.isPresent()) {
            salesReportForm = result.get();
        }
        else {
            throw new RuntimeException("Did not find permission id - " + id);
        }

        return salesReportForm;
    }

    @Transactional
    @Override
    public SalesReportForm save(SalesReportForm salesReportForm) {
        return salesReportFormRepository.save(salesReportForm);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        salesReportFormRepository.deleteById(id);
    }
}
