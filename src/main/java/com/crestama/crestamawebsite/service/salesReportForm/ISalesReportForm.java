package com.crestama.crestamawebsite.service.salesReportForm;

import com.crestama.crestamawebsite.entity.SalesReportForm;

import java.util.List;

public interface ISalesReportForm {
    List<SalesReportForm> findAll();

    SalesReportForm findById(Long id);

    SalesReportForm save(SalesReportForm salesReportForm);

    void deleteById(Long id);
}